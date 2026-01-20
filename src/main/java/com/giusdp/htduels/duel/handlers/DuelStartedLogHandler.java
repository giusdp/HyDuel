package com.giusdp.htduels.duel.handlers;

import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.MoveHandler;
import com.giusdp.htduels.duel.event.DuelEvent;

public class DuelStartedLogHandler implements MoveHandler {
    @Override
    public void handle(DuelEvent _move, Duel _duel) {
        System.out.println("[Duel] Duel started!");
    }
}
