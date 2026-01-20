package com.giusdp.htduels.duel.handlers;

import com.giusdp.htduels.duel.DuelEventHandler;
import com.giusdp.htduels.duel.event.DuelEvent;

public class DuelStartedLogHandler extends DuelEventHandler {
    @Override
    public void accept(DuelEvent event) {
        System.out.println("[Duel] Duel started!");
    }
}
