package com.giusdp.htduels.duel.phases;

import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.Phase;

public class WaitingPhase extends Phase {
    public static final String WAITING_MESSAGE = "Waiting for opponent...";
    public static final int MAX_WAIT_TICKS = 600;
    private int ticksWaited = 0;

    @Override
    public void onEnter(Duel duel) {
        onDuelistJoined(duel);
    }

    @Override
    public void tick(Duel duel) {
        if (ticksWaited >= MAX_WAIT_TICKS) {
            duel.transitionTo(new DuelEndPhase(DuelEndPhase.Reason.TIMEOUT));
            return;
        }
        ticksWaited++;
    }

    public void onDuelistJoined(Duel duel) {
        if (duel.getDuelists().size() >= 2) {
            duel.transitionTo(new StartupPhase());
            return;
        }

        duel.broadcastTurnIndicator(WAITING_MESSAGE);
    }

    @Override
    public void onExit(Duel duel) {
        duel.clearTurnIndicator();
    }
}
