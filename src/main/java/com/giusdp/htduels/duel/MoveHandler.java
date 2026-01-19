package com.giusdp.htduels.duel;

import com.giusdp.htduels.duel.moves.Move;

@FunctionalInterface
public interface MoveHandler {
    void handle(Move move, Duel duel);
}
