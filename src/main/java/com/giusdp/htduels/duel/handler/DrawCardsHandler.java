package com.giusdp.htduels.duel.handler;

import com.giusdp.htduels.CardRepo;
import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.duel.Card;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.event.DrawCards;
import com.giusdp.htduels.duel.event.DuelEvent;

public class DrawCardsHandler extends DuelEventHandler {
    private static final CardAsset PLACEHOLDER = new CardAsset("Placeholder", "Placeholder", 0, 1, 1, "Minion");

    private final CardRepo cardRepo;

    public DrawCardsHandler(Duel duel, CardRepo cardRepo) {
        this.cardRepo = cardRepo;
    }

    @Override
    public void accept(DuelEvent ev) {
        DrawCards drawCards = (DrawCards) ev;

        for (int i = 0; i < drawCards.count; i++) {
            Card card = new Card(PLACEHOLDER);
            drawCards.duelist.addToHand(card);
        }
    }

}
