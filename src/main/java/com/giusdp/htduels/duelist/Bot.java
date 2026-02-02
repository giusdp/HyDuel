package com.giusdp.htduels.duelist;

import com.giusdp.htduels.duel.Card;
import com.giusdp.htduels.duel.Duel;

public class Bot extends Duelist {
    public Bot() {
        super();
    }

    @Override
    public void takeTurn(Duel duel) {
        if (hand.getCards().isEmpty()) {
            duel.endMainPhase();
            return;
        }

        Card cardToPlay = hand.getCards().getFirst();
        duel.playCard(this, cardToPlay);
        duel.endMainPhase();
    }
}
