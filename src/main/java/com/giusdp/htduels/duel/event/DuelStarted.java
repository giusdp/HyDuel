package com.giusdp.htduels.duel.event;

import com.giusdp.htduels.duel.Duel;
import org.jspecify.annotations.NonNull;

public final class DuelStarted extends DuelEvent {
    public DuelStarted(@NonNull Duel duel) {
        super(duel);
    }
}
