package com.giusdp.htduels.duelist;

import com.giusdp.htduels.duel.Card;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.zone.Battlefield;
import com.giusdp.htduels.duel.zone.Hand;

public abstract class Duelist {
    protected final Hand hand;
    protected final Battlefield battlefield;
    private boolean opponentSide;

    protected Duelist() {
        this.hand = new Hand();
        this.battlefield = new Battlefield();
    }

    public boolean isOpponentSide() {
        return opponentSide;
    }

    public void setOpponentSide(boolean opponentSide) {
        this.opponentSide = opponentSide;
    }

    public Hand getHand() {
        return hand;
    }

    public Battlefield getBattlefield() {
        return battlefield;
    }

    public void addToHand(Card card) {
        card.setOwner(this);
        hand.place(card, 0);
    }

    public void playCard(Card card) {
        card.setOwner(this);
        battlefield.place(card, 0);
    }

    public abstract void takeTurn(Duel duel);
}
