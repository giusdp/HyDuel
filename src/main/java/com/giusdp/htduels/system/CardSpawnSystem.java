package com.giusdp.htduels.system;

import com.giusdp.htduels.PlayerDuelContext;
import com.giusdp.htduels.component.BoardLayoutComponent;
import com.giusdp.htduels.component.CardComponent;
import com.giusdp.htduels.component.DuelComponent;
import com.giusdp.htduels.duel.Card;
import com.giusdp.htduels.duel.positioning.BoardLayout;
import com.giusdp.htduels.duel.positioning.CardPositioningService;
import com.giusdp.htduels.duelist.Duelist;
import com.giusdp.htduels.spawn.CardSpawner;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.math.Vec2f;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CardSpawnSystem extends EntityTickingSystem<EntityStore> {

    @Override
    public void tick(float deltaTime, int index, @NonNull ArchetypeChunk<EntityStore> archetypeChunk,
                     @NonNull Store<EntityStore> store, @NonNull CommandBuffer<EntityStore> commandBuffer) {
        DuelComponent duelComponent = archetypeChunk.getComponent(index, DuelComponent.getComponentType());
        BoardLayoutComponent layoutComponent = archetypeChunk.getComponent(index, BoardLayoutComponent.getComponentType());

        if (duelComponent == null || layoutComponent == null) {
            return;
        }

        BoardLayout layout = layoutComponent.getBoardLayout();
        if (layout == null) {
            return;
        }

        Ref<EntityStore> duelRef = archetypeChunk.getReferenceTo(index);
        List<PlayerDuelContext> contexts = PlayerDuelContext.getByDuelRef(duelRef);
        if (contexts.isEmpty()) {
            return;
        }

        // Collect all cards that already have entities
        Set<Card> spawnedCards = new HashSet<>();
        for (PlayerDuelContext ctx : contexts) {
            for (Ref<EntityStore> cardRef : ctx.getCardEntities()) {
                CardComponent cardComp = store.getComponent(cardRef, CardComponent.getComponentType());
                if (cardComp != null) {
                    spawnedCards.add(cardComp.getCard());
                }
            }
        }

        float yawRadians = (float) layout.rotation().getRadians();

        // Check both duelists' hand and battlefield for unspawned cards
        spawnMissingCards(duelComponent.duel.duelist1, layout, yawRadians, duelRef, commandBuffer, spawnedCards, contexts);
        spawnMissingCards(duelComponent.duel.duelist2, layout, yawRadians, duelRef, commandBuffer, spawnedCards, contexts);
    }

    private void spawnMissingCards(Duelist duelist, BoardLayout layout, float yawRadians,
                                   Ref<EntityStore> duelRef, CommandBuffer<EntityStore> commandBuffer,
                                   Set<Card> spawnedCards, List<PlayerDuelContext> contexts) {
        Vector3f rotation = duelist.isBottomPlayer()
                ? new Vector3f((float) Math.PI, yawRadians, 0)
                : new Vector3f(0, yawRadians, 0);

        for (Card card : duelist.getHand().getCards()) {
            if (!spawnedCards.contains(card)) {
                spawnAndRegister(card, layout, layout.handYOffset(), rotation, duelRef, commandBuffer, contexts);
            }
        }

        for (Card card : duelist.getBattlefield().getCards()) {
            if (!spawnedCards.contains(card)) {
                spawnAndRegister(card, layout, layout.battlefieldYOffset(), rotation, duelRef, commandBuffer, contexts);
            }
        }
    }

    private void spawnAndRegister(Card card, BoardLayout layout, float y, Vector3f rotation,
                                  Ref<EntityStore> duelRef, CommandBuffer<EntityStore> commandBuffer,
                                  List<PlayerDuelContext> contexts) {
        Vec2f pos2d = CardPositioningService.getWorldPosition(card, layout);
        Vector3d pos = new Vector3d(pos2d.x, y, pos2d.y);
        Ref<EntityStore> cardRef = CardSpawner.spawn(commandBuffer, duelRef, card, pos, rotation);
        if (cardRef != null) {
            for (PlayerDuelContext ctx : contexts) {
                ctx.addCardEntity(cardRef);
            }
        }
    }

    @Override
    public @Nullable Query<EntityStore> getQuery() {
        return Query.and(DuelComponent.getComponentType(), BoardLayoutComponent.getComponentType());
    }
}
