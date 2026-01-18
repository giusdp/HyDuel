package com.giusdp.htduels.duel.phases;

import com.giusdp.htduels.components.Duel;
import com.giusdp.htduels.duel.Phase;
import com.hypixel.hytale.logger.HytaleLogger;

public class TurnEndPhase implements Phase {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    @Override
    public void onEnter(Duel duel) {
        LOGGER.atInfo().log("Entering TURN_END phase");
    }

    @Override
    public void tick(Duel duel) {
        LOGGER.atInfo().log("TURN_END tick");
        duel.transitionTo(new TurnStartPhase());
    }

    @Override
    public void onExit(Duel duel) {
        LOGGER.atInfo().log("Exiting TURN_END phase");
    }
}
