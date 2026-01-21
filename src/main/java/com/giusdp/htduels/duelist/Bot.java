package com.giusdp.htduels.duelist;

import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.event.EndMainPhase;
import com.giusdp.htduels.duel.event.PlayCard;

public class Bot extends Duelist {
    public Bot() {
        super();
    }

    public void playTurn(Duel duel) {
        if (hand.isEmpty()) {
            duel.emit(new EndMainPhase());
            return;
        }

        CardAsset cardToPlay = hand.getFirst();
        duel.emit(new PlayCard(this, cardToPlay));
        duel.emit(new EndMainPhase());
    }
}
