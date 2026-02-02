package com.giusdp.htduels.duel.event;

import com.giusdp.htduels.duel.Duel;
import org.jspecify.annotations.NonNull;

public class StartingDuelistSelected extends DuelEvent {
    public StartingDuelistSelected(@NonNull Duel duel) {
        super(duel);
    }
}
