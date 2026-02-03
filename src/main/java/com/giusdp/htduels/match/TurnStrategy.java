package com.giusdp.htduels.match;

import com.giusdp.htduels.match.Duel;

@FunctionalInterface
public interface TurnStrategy {
    void takeTurn(Duel duel, Duelist self);
}
