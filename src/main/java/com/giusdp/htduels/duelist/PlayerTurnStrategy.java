package com.giusdp.htduels.duelist;

import com.giusdp.htduels.duel.Duel;

public class PlayerTurnStrategy implements TurnStrategy {
    @Override
    public void takeTurn(Duel duel, Duelist self) {
        // Human input arrives asynchronously via mouse events — nothing to do per tick
    }
}
