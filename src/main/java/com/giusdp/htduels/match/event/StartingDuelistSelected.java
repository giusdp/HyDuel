package com.giusdp.htduels.match.event;

import com.giusdp.htduels.match.DuelId;
import org.jspecify.annotations.NonNull;

public class StartingDuelistSelected extends DuelEvent {
    public StartingDuelistSelected(@NonNull DuelId duelId) {
        super(duelId);
    }
}
