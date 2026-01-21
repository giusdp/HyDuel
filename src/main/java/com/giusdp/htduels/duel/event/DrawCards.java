package com.giusdp.htduels.duel.event;

import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duelist.Duelist;
import org.jspecify.annotations.NonNull;

public class DrawCards extends DuelEvent {
    public Duelist duelist;
    public int count;

    public DrawCards(@NonNull Duel duel, Duelist duelist, int count) {
        super(duel);
        this.duelist = duelist;
        this.count = count;
    }
}
