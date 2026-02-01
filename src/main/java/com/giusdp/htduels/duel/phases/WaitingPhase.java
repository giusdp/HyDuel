package com.giusdp.htduels.duel.phases;

import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.Phase;

public class WaitingPhase extends Phase {
    public static final int MAX_WAIT_TICKS = 600;
    private int ticksWaited = 0;

    @Override
    public void onEnter(Duel duel) {
    }

    @Override
    public void tick(Duel duel) {
        if (duel.getDuelists().size() >= 2) {
            duel.transitionTo(new StartupPhase());
            return;
        }
        if (ticksWaited >= MAX_WAIT_TICKS) {
            duel.transitionTo(new DuelEndPhase(DuelEndPhase.Reason.TIMEOUT));
            return;
        }
        ticksWaited++;
    }

    @Override
    public void onExit(Duel duel) {
    }

    public int getTicksWaited() {
        return ticksWaited;
    }
}
