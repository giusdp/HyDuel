package com.giusdp.htduels.duelist;

import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.duel.Card;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.event.EndMainPhase;
import com.giusdp.htduels.duel.event.PlayCard;

public class Bot extends Duelist {
    public Bot() {
        super();
    }

    public void playTurn(Duel duel) {
        if (hand.getCards().isEmpty()) {
            duel.emit(new EndMainPhase(duel));
            return;
        }

        Card cardToPlay = hand.getCards().getFirst();
        duel.emit(new PlayCard(duel, this, cardToPlay));
        duel.emit(new EndMainPhase(duel));
    }
}
