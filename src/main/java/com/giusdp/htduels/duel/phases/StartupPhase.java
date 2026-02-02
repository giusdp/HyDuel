package com.giusdp.htduels.duel.phases;

import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.Phase;
import com.giusdp.htduels.duel.event.DuelStarted;

public class StartupPhase extends Phase {
    private int drawCount = 0;

    @Override
    public void onEnter(Duel duel) {
        System.out.println("[Duel] Duel started!");
        duel.recordEvent(new DuelStarted(duel.getId()));
        duel.selectStartingDuelist();
    }

    @Override
    public void tick(Duel duel) {
        if (drawCount < 5) {
            duel.drawCards(duel.getDuelist(0), 1);
        } else if (drawCount < 10) {
            duel.drawCards(duel.getDuelist(1), 1);
        } else {
            duel.transitionTo(new TurnStartPhase());
            return;
        }
        drawCount++;
    }

    @Override
    public void onExit(Duel duel) {
    }
}
