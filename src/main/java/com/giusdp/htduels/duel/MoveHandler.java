package com.giusdp.htduels.duel;

import com.giusdp.htduels.duel.event.DuelEvent;

@FunctionalInterface
public interface MoveHandler {
    void handle(DuelEvent move, Duel duel);
}
