package com.giusdp.htduels.match.event;

import com.giusdp.htduels.match.DuelId;
import org.jspecify.annotations.NonNull;

public abstract class DuelEvent {
    public final DuelId duelId;

    public DuelEvent(@NonNull DuelId duelId) {
        this.duelId = duelId;
    }
}
