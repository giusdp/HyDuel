package com.giusdp.htduels.duel.phases;

import com.giusdp.htduels.components.Duel;
import com.giusdp.htduels.duel.Phase;
import com.hypixel.hytale.logger.HytaleLogger;

public class StartupPhase implements Phase {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    @Override
    public void onEnter(Duel duel) {
        LOGGER.atInfo().log("Entering STARTUP phase");
    }

    @Override
    public void tick(Duel duel) {
        LOGGER.atInfo().log("STARTUP tick");
        duel.transitionTo(new TurnStartPhase());
    }

    @Override
    public void onExit(Duel duel) {
        LOGGER.atInfo().log("Exiting STARTUP phase");
    }
}
