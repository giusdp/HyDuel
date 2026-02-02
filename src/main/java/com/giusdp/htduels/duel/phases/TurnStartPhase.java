package com.giusdp.htduels.duel.phases;

import com.giusdp.htduels.DuelistContext;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.Phase;
import com.giusdp.htduels.duelist.Duelist;
import com.giusdp.htduels.ui.BoardGameUi;

public class TurnStartPhase extends Phase {

    @Override
    public void onEnter(Duel duel) {
        duel.drawCards(duel.getActiveDuelist(), 1);

        updateTurnIndicators(duel);
    }

    private void updateTurnIndicators(Duel duel) {
        for (DuelistContext ctx : duel.getContexts()) {
            BoardGameUi ui = ctx.getBoardGameUi();
            if (ui == null) continue;

            String text = turnIndicatorText(ctx.getDuelist(), duel.getActiveDuelist());
            ui.updateTurnIndicator(text);
        }
    }

    public static String turnIndicatorText(Duelist playerDuelist, Duelist activeDuelist) {
        return playerDuelist == activeDuelist ? "Your Turn" : "Opponent's Turn";
    }

    @Override
    public void tick(Duel duel) {

        duel.transitionTo(new MainPhase());
    }

    @Override
    public void onExit(Duel duel) {
    }
}
