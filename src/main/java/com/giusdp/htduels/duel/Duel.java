package com.giusdp.htduels.duel;

import com.giusdp.htduels.duel.phases.StartupPhase;

import java.util.List;

public class Duel {

    public Hand[] playerHands;
    public Phase currentPhase;

    public Duel() {
        playerHands = new Hand[2];
        playerHands[0] = new Hand();
        playerHands[1] = new Hand();
        currentPhase = new StartupPhase();
        currentPhase.onEnter(this);
    }

    public void tick() {
        currentPhase.tick(this);
    }

    public void transitionTo(Phase newPhase) {
        currentPhase.onExit(this);
        currentPhase = newPhase;
        currentPhase.onEnter(this);
    }
}
