package com.giusdp.htduels.duel.zone;

import com.giusdp.htduels.duel.Card;

import java.util.ArrayList;
import java.util.List;

public class Battlefield implements Zone {
    List<Card> cards;

    public Battlefield() {
        cards = new ArrayList<>();
    }

    @Override
    public ZoneType getType() {
        return ZoneType.BATTLEFIELD;
    }

    @Override
    public List<Card> getCards() {
        return cards;
    }

    @Override
    public void add(Card card, int index) {
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
