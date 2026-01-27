package com.giusdp.htduels.duel.event;

import com.giusdp.htduels.duel.Card;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duelist.Duelist;
import org.jspecify.annotations.NonNull;

public class CardClicked extends DuelEvent {
    public final Card card;
    public final Duelist clicker;

    public CardClicked(@NonNull Duel duel, @NonNull Card card, @NonNull Duelist clicker) {
        super(duel);
        this.card = card;
        this.clicker = clicker;
    }
}
