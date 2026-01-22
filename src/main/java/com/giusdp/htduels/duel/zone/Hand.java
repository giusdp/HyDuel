package com.giusdp.htduels.duel.zone;

import com.giusdp.htduels.duel.Card;

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
    public void add(Card card, int index) {
        cards.add(index, card);

    }

    @Override
    public void remove(Card card) {
        cards.remove(card);
    }
}
