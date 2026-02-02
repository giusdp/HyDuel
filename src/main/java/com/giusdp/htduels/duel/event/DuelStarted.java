package com.giusdp.htduels.duel.event;

import com.giusdp.htduels.duel.DuelId;
import org.jspecify.annotations.NonNull;

public final class DuelStarted extends DuelEvent {
    public DuelStarted(@NonNull DuelId duelId) {
        super(duelId);
    }
}
