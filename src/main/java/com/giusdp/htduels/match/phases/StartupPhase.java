package com.giusdp.htduels.match.phases;

import com.giusdp.htduels.match.Duel;
import com.giusdp.htduels.match.DuelPhase;
import com.giusdp.htduels.match.event.DuelStarted;

public class StartupPhase extends DuelPhase {
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
