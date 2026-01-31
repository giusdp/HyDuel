package com.giusdp.htduels.duel.phases;

import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.Phase;
import com.giusdp.htduels.duel.event.DrawCards;
import com.giusdp.htduels.duel.event.DuelStarted;
import com.giusdp.htduels.duel.event.RandomDuelistSelect;

public class StartupPhase extends Phase {
    private int drawCount = 0;

    @Override
    public void onEnter(Duel duel) {
        System.out.println("[Duel] Duel started!");
        duel.emit(new DuelStarted(duel));
        duel.emit(new RandomDuelistSelect(duel));
    }

    @Override
    public void tick(Duel duel) {
        if (drawCount < 5) {
            duel.emit(new DrawCards(duel, duel.getDuelist(0), 1));
        } else if (drawCount < 10) {
            duel.emit(new DrawCards(duel, duel.getDuelist(1), 1));
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
