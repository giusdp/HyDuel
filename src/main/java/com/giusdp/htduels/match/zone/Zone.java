package com.giusdp.htduels.match.zone;

import com.giusdp.htduels.match.Card;

import java.util.List;

public interface Zone {
    ZoneType getType();

    List<Card> getCards();

    void place(Card card, int index);

    void remove(Card card);
}
