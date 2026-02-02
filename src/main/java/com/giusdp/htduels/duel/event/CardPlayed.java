package com.giusdp.htduels.duel.event;

import com.giusdp.htduels.duel.Card;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duelist.Duelist;
import org.jspecify.annotations.NonNull;

public class CardPlayed extends DuelEvent {
    public Duelist duelist;
    public Card card;

    public CardPlayed(@NonNull Duel duel, Duelist duelist, Card card) {
        super(duel);
        this.duelist = duelist;
        this.card = card;
    }
}
