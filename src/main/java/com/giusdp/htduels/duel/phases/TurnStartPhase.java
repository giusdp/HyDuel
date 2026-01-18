package com.giusdp.htduels.duel.phases;

import com.giusdp.htduels.components.Duel;
import com.giusdp.htduels.duel.Phase;
import com.hypixel.hytale.logger.HytaleLogger;

public class TurnStartPhase implements Phase {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    @Override
    public void onEnter(Duel duel) {
        LOGGER.atInfo().log("Entering TURN_START phase");
    }

    @Override
    public void tick(Duel duel) {
        LOGGER.atInfo().log("TURN_START tick");
        duel.transitionTo(new MainPhase());
    }

    @Override
    public void onExit(Duel duel) {
        LOGGER.atInfo().log("Exiting TURN_START phase");
    }
}
