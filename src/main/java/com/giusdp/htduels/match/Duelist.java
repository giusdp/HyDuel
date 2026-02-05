package com.giusdp.htduels.match;

import com.giusdp.htduels.match.zone.Battlefield;
import com.giusdp.htduels.match.zone.Deck;
import com.giusdp.htduels.match.zone.Hand;

import java.util.List;

public class Duelist {
    private final Hand hand;
    private final Battlefield battlefield;
    private final Deck deck = new Deck();
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

    public Deck getDeck() {
        return deck;
    }

    public void initializeDeck(List<Card> cards) {
        for (int i = 0; i < cards.size(); i++) {
            deck.place(cards.get(i), i);
        }
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
