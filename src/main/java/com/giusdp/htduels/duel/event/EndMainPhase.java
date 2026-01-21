package com.giusdp.htduels.duel.event;

import com.giusdp.htduels.duel.Duel;
import org.jspecify.annotations.NonNull;

public class EndMainPhase extends DuelEvent {
    public EndMainPhase(@NonNull Duel duel) {
        super(duel);
    }
}
