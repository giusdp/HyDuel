package com.giusdp.htduels.match.event;

import com.giusdp.htduels.match.DuelId;
import org.jspecify.annotations.NonNull;

public final class DuelistJoined extends DuelEvent {

    public DuelistJoined(@NonNull DuelId duelId) {
        super(duelId);
    }
}
