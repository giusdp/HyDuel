package com.giusdp.htduels.match.zone;

import com.giusdp.htduels.match.Card;

import java.util.ArrayList;
import java.util.List;

public class Hand implements Zone {
    List<Card> cards;

    public Hand() {
        cards = new ArrayList<>();
    }

    @Override
    public ZoneType getType() {
        return ZoneType.HAND;
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
}
