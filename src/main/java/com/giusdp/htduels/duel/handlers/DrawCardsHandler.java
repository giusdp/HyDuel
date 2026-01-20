package com.giusdp.htduels.duel.handlers;

import com.giusdp.htduels.CardRepo;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.MoveHandler;
import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.duel.event.DrawCards;
import com.giusdp.htduels.duel.event.DuelEvent;

import java.util.ArrayList;
import java.util.List;

public class DrawCardsHandler implements MoveHandler {

    private static final CardAsset PLACEHOLDER = new CardAsset("Placeholder", "Placeholder", 0, 1, 1, "Minion");

    private final CardRepo cardRepo;

    public DrawCardsHandler(CardRepo cardRepo) {
        this.cardRepo = cardRepo;
    }

    @Override
    public void handle(DuelEvent move, Duel duel) {
        DrawCards drawCards = (DrawCards) move;
        List<CardAsset> availableCards = new ArrayList<>(cardRepo.getAvailableCards());

        for (int i = 0; i < drawCards.count(); i++) {
            CardAsset card = availableCards.isEmpty()
                ? PLACEHOLDER
                : availableCards.get(i % availableCards.size());
            duel.playerHands[drawCards.playerIndex()].cards.add(card);
        }
    }
}
