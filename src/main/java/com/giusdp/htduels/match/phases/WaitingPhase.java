package com.giusdp.htduels.match.phases;

import com.giusdp.htduels.match.Duel;
import com.giusdp.htduels.match.DuelPhase;
import com.giusdp.htduels.match.event.DuelCancelled;

public class WaitingPhase extends DuelPhase {
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
            duel.transitionTo(new DuelCancelledPhase(DuelCancelled.Reason.NO_OPPONENT));
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
