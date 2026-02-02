package com.giusdp.htduels.duel.event;

import com.giusdp.htduels.duel.DuelId;
import org.jspecify.annotations.NonNull;

public final class DuelistJoined extends DuelEvent {

    public DuelistJoined(@NonNull DuelId duelId) {
        super(duelId);
    }
}
