package com.giusdp.htduels.duel.handlers;

import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.event.DuelEvent;
import org.jspecify.annotations.NonNull;

public class DuelStartedLogHandler extends DuelEventHandler {
    public DuelStartedLogHandler(@NonNull Duel duel) {
        super(duel);
    }

    @Override
    public void accept(DuelEvent event) {
        System.out.println("[Duel] Duel started!");
    }
}
