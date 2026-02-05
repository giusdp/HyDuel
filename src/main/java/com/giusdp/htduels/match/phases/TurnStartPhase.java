package com.giusdp.htduels.match.phases;

import com.giusdp.htduels.hytale.DuelistSessionManager;
import com.giusdp.htduels.match.Duel;
import com.giusdp.htduels.match.DuelPhase;
import com.giusdp.htduels.match.Duelist;
import com.giusdp.htduels.hytale.ui.BoardGameUi;

public class TurnStartPhase extends DuelPhase {

    @Override
    public void onEnter(Duel duel) {
        duel.drawCards(duel.getActiveDuelist(), 1);

        updateTurnIndicators(duel);
    }

    private void updateTurnIndicators(Duel duel) {
        for (DuelistSessionManager ctx : duel.getContexts()) {
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
