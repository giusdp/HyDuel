package com.giusdp.htduels.duel.event;

import com.giusdp.htduels.duel.Duel;
import org.jspecify.annotations.NonNull;

public abstract class DuelEvent {
    public final Duel duel;

    public DuelEvent(@NonNull Duel duel) {
        this.duel = duel;
    }
}
