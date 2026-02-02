package com.giusdp.htduels.duel.event;

import com.giusdp.htduels.duel.Duel;
import org.jspecify.annotations.NonNull;

public class MainPhaseEnded extends DuelEvent {
    public MainPhaseEnded(@NonNull Duel duel) {
        super(duel);
    }
}
