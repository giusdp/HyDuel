package com.giusdp.htduels.duel.event;

import com.giusdp.htduels.duel.Card;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duelist.Duelist;
import org.jspecify.annotations.NonNull;

public class PlayCard extends DuelEvent {
    public Duelist duelist;
    public Card card;

    public PlayCard(@NonNull Duel duel, Duelist duelist, Card card) {
        super(duel);
        this.duelist = duelist;
        this.card = card;
    }
}
