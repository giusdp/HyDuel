package com.giusdp.htduels.match;
import com.giusdp.htduels.catalog.CardAssetRepo;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DuelService {

    private final DuelRegistry registry;

    public DuelService(DuelRegistry registry) {
        this.registry = registry;
    }

    public Duel createDuel(Vector3i boardPosition) {
        Duel duel = Duel.builder()
                .cardRepo(new CardAssetRepo())
                .boardPosition(boardPosition)
                .build();
        registry.registerDuel(duel.getId(), duel);
        return duel;
    }

    public Duelist addHumanDuelist(Duel duel, List<String> cardIds) {
        Duelist humanDuelist = new Duelist(new HumanTurnStrategy());
        boolean isOpponentSide = !duel.getDuelists().isEmpty();
        humanDuelist.setOpponentSide(isOpponentSide);

        List<Card> cards = createCardsFromIds(duel, cardIds);
        humanDuelist.initializeDeck(cards);

        duel.addDuelist(humanDuelist);
        return humanDuelist;
    }

    public Duelist addBotDuelist(Duel duel, List<String> cardIds) {
        Duelist botDuelist = new Duelist(new BotTurnStrategy());
        boolean isOpponentSide = !duel.getDuelists().isEmpty();
        botDuelist.setOpponentSide(isOpponentSide);

        List<Card> cards = createCardsFromIds(duel, cardIds);
        botDuelist.initializeDeck(cards);

        duel.addDuelist(botDuelist);
        return botDuelist;
    }

    private List<Card> createCardsFromIds(Duel duel, List<String> cardIds) {
        CardRepo cardRepo = duel.getCardRepo();
        List<Card> cards = new ArrayList<>();
        for (String cardId : cardIds) {
            cardRepo.findById(cardId).ifPresent(asset -> cards.add(new Card(asset)));
        }
        return cards;
    }

    @Nullable
    public Duel findDuel(DuelId duelId) {
        return registry.findDuel(duelId);
    }

    @Nullable
    public Ref<EntityStore> findDuelAt(Vector3i boardPosition) {
        return registry.findDuelAt(boardPosition);
    }

    public void removeDuel(Vector3i boardPosition) {
        registry.removeDuel(boardPosition);
    }

    public void removeDuelById(DuelId duelId) {
        registry.removeDuelById(duelId);
    }
}
