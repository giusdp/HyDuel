package com.giusdp.htduels.duelist;

import com.giusdp.htduels.duel.Duel;

@FunctionalInterface
public interface TurnStrategy {
    void takeTurn(Duel duel, Duelist self);
}
