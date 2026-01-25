package com.giusdp.htduels.duel.event;

import com.giusdp.htduels.duel.Duel;
import com.hypixel.hytale.event.IEvent;
import org.jspecify.annotations.NonNull;

public abstract class DuelEvent implements IEvent<Void> {
    public final Duel duel;

    public DuelEvent(@NonNull Duel duel) {
        this.duel = duel;
    }
}
