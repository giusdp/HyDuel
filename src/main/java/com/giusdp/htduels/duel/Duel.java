package com.giusdp.htduels.duel;

import com.giusdp.htduels.duel.phases.StartupPhase;

public class Duel {

    private Phase currentPhase = new StartupPhase();

    public void tick() {
        currentPhase.tick(this);
    }

    public void transitionTo(Phase newPhase) {
        currentPhase.onExit(this);
        currentPhase = newPhase;
        currentPhase.onEnter(this);
    }

    public Phase getCurrentPhase() {
        return currentPhase;
    }
}
