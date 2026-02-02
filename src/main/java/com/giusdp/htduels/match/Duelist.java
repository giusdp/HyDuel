package com.giusdp.htduels.match;

import com.giusdp.htduels.match.Card;
import com.giusdp.htduels.match.Duel;
import com.giusdp.htduels.match.zone.Battlefield;
import com.giusdp.htduels.match.zone.Hand;

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
