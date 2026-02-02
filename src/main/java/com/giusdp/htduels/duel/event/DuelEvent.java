package com.giusdp.htduels.duel.event;

import com.giusdp.htduels.duel.DuelId;
import org.jspecify.annotations.NonNull;

public abstract class DuelEvent {
    public final DuelId duelId;

    public DuelEvent(@NonNull DuelId duelId) {
        this.duelId = duelId;
    }
}
