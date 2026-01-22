package com.giusdp.htduels.duelist;

import com.giusdp.htduels.duel.Card;
import com.giusdp.htduels.duel.zone.Battlefield;
import com.giusdp.htduels.duel.zone.Hand;

public abstract class Duelist {
    protected final Hand hand;
    protected final Battlefield battlefield;

    protected Duelist() {
        this.hand = new Hand();
        this.battlefield = new Battlefield();
    }

    public Hand getHand() {
        return hand;
    }

    public Battlefield getBattlefield() {
        return battlefield;
    }

    public void addToHand(Card card) {
        hand.add(card, 0);
    }

    public void playCard(Card card) {
        hand.remove(card);
        battlefield.add(card, 0);
    }
}
