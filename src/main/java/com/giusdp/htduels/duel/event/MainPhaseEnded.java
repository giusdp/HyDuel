package com.giusdp.htduels.duel.event;

import com.giusdp.htduels.duel.DuelId;
import org.jspecify.annotations.NonNull;

public class MainPhaseEnded extends DuelEvent {
    public MainPhaseEnded(@NonNull DuelId duelId) {
        super(duelId);
    }
}
