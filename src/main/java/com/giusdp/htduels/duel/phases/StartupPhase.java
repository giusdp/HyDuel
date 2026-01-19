package com.giusdp.htduels.duel.phases;

import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.Phase;
import com.giusdp.htduels.duel.moves.DrawCards;

public class StartupPhase extends Phase {

    @Override
    public void onEnter(Duel duel) {
        duel.emit(new DrawCards(0, 5));
        duel.emit(new DrawCards(1, 5));
    }

    @Override
    public void tick(Duel duel) {
        duel.transitionTo(new TurnStartPhase());
    }

    @Override
    public void onExit(Duel duel) {
    }
}
