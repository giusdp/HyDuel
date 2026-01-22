package com.giusdp.htduels.duel.zone;

import com.giusdp.htduels.duel.Card;

import java.util.List;

public interface Zone {
    ZoneType getType();

    List<Card> getCards();

    void add(Card card, int index);

    void remove(Card card);
}
