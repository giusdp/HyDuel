package com.giusdp.htduels.duel.event;

import com.giusdp.htduels.duel.DuelId;
import org.jspecify.annotations.NonNull;

public class StartingDuelistSelected extends DuelEvent {
    public StartingDuelistSelected(@NonNull DuelId duelId) {
        super(duelId);
    }
}
