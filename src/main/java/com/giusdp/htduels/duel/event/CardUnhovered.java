package com.giusdp.htduels.duel.event;

import com.giusdp.htduels.duel.Card;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duelist.Duelist;
import org.jspecify.annotations.NonNull;

public class CardUnhovered extends DuelEvent {
    public final Card card;
    public final Duelist viewer;

    public CardUnhovered(@NonNull Duel duel, @NonNull Card card, @NonNull Duelist viewer) {
        super(duel);
        this.card = card;
        this.viewer = viewer;
    }
}
