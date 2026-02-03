package com.giusdp.htduels.match.phases;

import com.giusdp.htduels.match.Duel;
import com.giusdp.htduels.match.DuelPhase;

public class MainPhase extends DuelPhase {

    @Override
    public void onEnter(Duel duel) {
    }

    @Override
    public void tick(Duel duel) {
        duel.getActiveDuelist().takeTurn(duel);
    }

    @Override
    public void onExit(Duel duel) {
    }
}
