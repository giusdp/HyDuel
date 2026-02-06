package com.giusdp.htduels.hytale;

import com.giusdp.htduels.match.DuelRegistry;
import com.giusdp.htduels.hytale.ecs.component.CardComponent;
import com.giusdp.htduels.hytale.ecs.component.DuelComponent;
import com.giusdp.htduels.match.Card;
import com.giusdp.htduels.match.CardId;
import com.giusdp.htduels.match.Duel;
import com.giusdp.htduels.match.DuelId;
import com.giusdp.htduels.match.event.CardPlayed;
import com.giusdp.htduels.match.event.CardsDrawn;
import com.giusdp.htduels.match.event.DuelCancelled;
import com.giusdp.htduels.match.event.DuelEnded;
import com.giusdp.htduels.match.event.DuelEvent;
import com.giusdp.htduels.hytale.layout.BoardLayout;
import com.giusdp.htduels.hytale.layout.CardPositioningService;
import com.giusdp.htduels.match.zone.ZoneType;
import com.giusdp.htduels.match.Duelist;
import com.giusdp.htduels.hytale.ecs.CardSpawner;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.math.Vec2f;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;

import java.util.List;

public class DomainEventSync {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private final DuelRegistry registry;

    public DomainEventSync(DuelRegistry registry) {
        this.registry = registry;
    }

    public void process(List<DuelEvent> events, DuelId duelId,
                        DuelComponent duelComp, BoardLayout layout,
                        Ref<EntityStore> duelRef, CommandBuffer<EntityStore> commandBuffer) {
        Duel duel = registry.findDuel(duelId);
        if (duel == null) return;

        for (DuelEvent event : events) {
            // TODO refactor this with some generic handler thingy to not have an evergrowing if else if
            if (event instanceof CardsDrawn drawn) {
                handleCardsDrawn(drawn, duel, duelComp, layout, duelRef, commandBuffer);
            } else if (event instanceof CardPlayed played) {
                handleCardPlayed(played, duel, duelComp, duelRef);
            } else if (event instanceof DuelEnded ended) {
                handleDuelEnded(ended, duel);
            } else if (event instanceof DuelCancelled cancelled) {
                handleDuelCancelled(cancelled, duel);
            }
        }
    }

    private void handleCardsDrawn(CardsDrawn drawn, Duel duel, DuelComponent duelComp,
                                   BoardLayout layout, Ref<EntityStore> duelRef,
                                   CommandBuffer<EntityStore> commandBuffer) {
        float yawRadians = (float) layout.rotation().getRadians();

        for (CardId cardId : drawn.cardIds) {
            Card card = duel.findCard(cardId);
            if (card == null) continue;

            ZoneType zoneType = card.getCurrentZoneType();
            int zoneIndex = card.getZoneIndex();
            Duelist owner = card.getOwner();
            boolean opponentSide = owner != null && owner.isOpponentSide();
            int zoneSize = getZoneSize(card);

            Vec2f pos2d = CardPositioningService.getWorldPosition(zoneType, zoneIndex, zoneSize, opponentSide, layout);
            float y = zoneType == ZoneType.BATTLEFIELD ? layout.battlefieldYOffset() : layout.handYOffset();
            Vector3d position = new Vector3d(pos2d.x, y, pos2d.y);

            Vector3f rotation = opponentSide
                    ? new Vector3f((float) Math.PI, yawRadians, 0)
                    : new Vector3f(0, yawRadians, 0);

            Ref<EntityStore> cardRef = CardSpawner.spawn(commandBuffer, duelRef, cardId,
                    zoneType, zoneIndex, zoneSize, opponentSide, position, rotation);

            if (cardRef != null) {
                duelComp.addCardEntity(cardRef);
                for (DuelistSessionManager ctx : duel.getContexts()) {
                    if (ctx.getDuelist() == owner) {
                        ctx.addCardEntity(cardRef);
                        break;
                    }
                }
            }
        }
    }

    private void handleCardPlayed(CardPlayed played, Duel duel, DuelComponent duelComp,
                                   Ref<EntityStore> duelRef) {
        Card card = duel.findCard(played.cardId);
        if (card == null) return;

        // Update moved card and recalculate siblings in affected zones
        var store = duelRef.getStore();
        for (Ref<EntityStore> entityRef : duelComp.getCardEntities()) {
            CardComponent cc = store.getComponent(entityRef, CardComponent.getComponentType());
            if (cc == null) continue;

            Card entityCard = duel.findCard(cc.getCardId());
            if (entityCard == null) continue;

            ZoneType newZoneType = entityCard.getCurrentZoneType();
            if (newZoneType == null) {
                LOGGER.atWarning().log("Card %s has null zone type during CardPlayed sync", cc.getCardId());
                continue;
            }

            int newIndex = entityCard.getZoneIndex();
            int newSize = getZoneSize(entityCard);
            boolean newOpponentSide = entityCard.getOwner() != null && entityCard.getOwner().isOpponentSide();

            if (cc.getZoneType() != newZoneType || cc.getZoneIndex() != newIndex
                    || cc.getZoneSize() != newSize || cc.isOpponentSide() != newOpponentSide) {
                cc.setZoneType(newZoneType);
                cc.setZoneIndex(newIndex);
                cc.setZoneSize(newSize);
                cc.setOpponentSide(newOpponentSide);
            }
        }
    }

    private static int getZoneSize(Card card) {
        if (card.getZone() == null) return 0;
        return card.getZone().getCards().size();
    }

    private void handleDuelEnded(DuelEnded ended, Duel duel) {
        Duelist winner = duel.getDuelist(ended.winnerIndex);
        Duelist loser = duel.getDuelist(ended.loserIndex);

        String reasonText = switch (ended.reason) {
            case DECK_OUT -> "ran out of cards";
            case FORFEIT -> "forfeited";
            case WIN -> "was defeated";
        };

        for (DuelistSessionManager ctx : duel.getContexts()) {
            PlayerRef playerRef = ctx.getPlayerRef();
            if (playerRef == null) continue;

            Ref<EntityStore> playerEntityRef = playerRef.getReference();
            if (playerEntityRef == null) continue;

            Store<EntityStore> store = playerEntityRef.getStore();
            Player player = store.getComponent(playerEntityRef, Player.getComponentType());
            if (player == null) continue;

            if (ctx.getDuelist() == winner) {
                player.sendMessage(Message.raw("You win! Your opponent " + reasonText + "."));
            } else if (ctx.getDuelist() == loser) {
                player.sendMessage(Message.raw("You lose! You " + reasonText + "."));
            }
        }
    }

    private void handleDuelCancelled(DuelCancelled cancelled, Duel duel) {
        String reasonText = switch (cancelled.reason) {
            case NO_OPPONENT -> "No opponent joined in time.";
        };

        for (DuelistSessionManager ctx : duel.getContexts()) {
            PlayerRef playerRef = ctx.getPlayerRef();
            if (playerRef == null) continue;

            Ref<EntityStore> playerEntityRef = playerRef.getReference();
            if (playerEntityRef == null) continue;

            Store<EntityStore> store = playerEntityRef.getStore();
            Player player = store.getComponent(playerEntityRef, Player.getComponentType());
            if (player == null) continue;

            player.sendMessage(Message.raw("Duel cancelled: " + reasonText));
        }
    }
}
