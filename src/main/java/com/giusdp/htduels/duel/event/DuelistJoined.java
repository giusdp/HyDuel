package com.giusdp.htduels.duel.event;

import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duelist.Duelist;
import org.jspecify.annotations.NonNull;

public final class DuelistJoined extends DuelEvent {
    public final Duelist duelist;

    public DuelistJoined(@NonNull Duel duel, @NonNull Duelist duelist) {
        super(duel);
        this.duelist = duelist;
    }
}
