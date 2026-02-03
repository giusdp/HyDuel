package com.giusdp.htduels.match.event;

import com.giusdp.htduels.match.DuelId;
import org.jspecify.annotations.NonNull;

public final class DuelStarted extends DuelEvent {
    public DuelStarted(@NonNull DuelId duelId) {
        super(duelId);
    }
}
