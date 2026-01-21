package com.giusdp.htduels.duel.handlers;

import com.giusdp.htduels.CardRepo;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.duel.event.DrawCards;
import com.giusdp.htduels.duel.event.DuelEvent;

import java.util.ArrayList;
import java.util.List;

public class DrawCardsHandler extends DuelEventHandler {

    private static final CardAsset PLACEHOLDER = new CardAsset("Placeholder", "Placeholder", 0, 1, 1, "Minion");

    private final CardRepo cardRepo;

    public DrawCardsHandler(Duel duel, CardRepo cardRepo) {
        super(duel);
        this.cardRepo = cardRepo;
    }

    @Override
    public void accept(DuelEvent ev) {
        DrawCards drawCards = (DrawCards) ev;
        List<CardAsset> availableCards = new ArrayList<>(cardRepo.getAvailableCards());

        for (int i = 0; i < drawCards.count(); i++) {
            CardAsset card = availableCards.isEmpty()
                ? PLACEHOLDER
                : availableCards.get(i % availableCards.size());
           drawCards.duelist().addToHand(card);
        }
    }

}
