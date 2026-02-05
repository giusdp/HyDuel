package com.giusdp.htduels.match.zone;

import com.giusdp.htduels.match.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck implements Zone {
    private final List<Card> cards = new ArrayList<>();

    @Override
    public ZoneType getType() {
        return ZoneType.DECK;
    }

    @Override
    public List<Card> getCards() {
        return cards;
    }

    @Override
    public void place(Card card, int index) {
        if (card.getZone() != null) {
            card.getZone().remove(card);
        }
        cards.add(index, card);
        card.setZone(this);
    }

    @Override
    public void remove(Card card) {
        cards.remove(card);
        card.setZone(null);
    }

    // Deck-specific methods

    /**
     * Draws from top (end of list). Returns null if empty.
     */
    public Card drawTop() {
        if (cards.isEmpty()) {
            return null;
        }
        Card card = cards.removeLast();
        card.setZone(null);
        return card;
    }

    /**
     * Randomizes card order.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public int size() {
        return cards.size();
    }
}
