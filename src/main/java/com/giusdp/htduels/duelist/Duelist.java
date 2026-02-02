package com.giusdp.htduels.duelist;

import com.giusdp.htduels.duel.Card;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.zone.Battlefield;
import com.giusdp.htduels.duel.zone.Hand;

public class Duelist {
    private final Hand hand;
    private final Battlefield battlefield;
    private final TurnStrategy turnStrategy;
    private boolean opponentSide;

    public Duelist(TurnStrategy turnStrategy) {
        this.hand = new Hand();
        this.battlefield = new Battlefield();
        this.turnStrategy = turnStrategy;
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

    public void takeTurn(Duel duel) {
        turnStrategy.takeTurn(duel, this);
    }
}
