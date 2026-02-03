package com.giusdp.htduels.match.event;

import com.giusdp.htduels.match.DuelId;
import org.jspecify.annotations.NonNull;

public class MainPhaseEnded extends DuelEvent {
    public MainPhaseEnded(@NonNull DuelId duelId) {
        super(duelId);
    }
}
