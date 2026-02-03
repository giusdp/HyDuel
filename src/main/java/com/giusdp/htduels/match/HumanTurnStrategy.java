package com.giusdp.htduels.match;

import com.giusdp.htduels.match.Duel;

public class HumanTurnStrategy implements TurnStrategy {
    @Override
    public void takeTurn(Duel duel, Duelist self) {
        // Human input arrives asynchronously via mouse events — nothing to do per tick
    }
}
