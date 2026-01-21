package com.giusdp.htduels.duel.event;

import com.giusdp.htduels.duel.Duel;
import org.jspecify.annotations.NonNull;

public class RandomDuelistSelect extends DuelEvent {
    public RandomDuelistSelect(@NonNull Duel duel) {
        super(duel);
    }
}
