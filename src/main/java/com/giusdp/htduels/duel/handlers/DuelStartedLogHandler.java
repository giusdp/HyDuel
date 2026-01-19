package com.giusdp.htduels.duel.handlers;

import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.MoveHandler;
import com.giusdp.htduels.duel.moves.Move;

public class DuelStartedLogHandler implements MoveHandler {
    @Override
    public void handle(Move _move, Duel _duel) {
        System.out.println("[Duel] Duel started!");
    }
}
