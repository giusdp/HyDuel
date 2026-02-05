package com.giusdp.htduels.match.phases;

import com.giusdp.htduels.match.Duel;
import com.giusdp.htduels.match.DuelPhase;
import com.giusdp.htduels.match.event.DuelEnded;

public class DuelEndPhase extends DuelPhase {

    public enum Reason {
        FORFEIT, WIN, TIMEOUT, DECK_OUT
    }

    public final Reason reason;
    private final int winnerIndex;
    private final int loserIndex;

    public DuelEndPhase(Reason reason, int winnerIndex, int loserIndex) {
        this.reason = reason;
        this.winnerIndex = winnerIndex;
        this.loserIndex = loserIndex;
    }

    @Override
    public void onEnter(Duel duel) {
        duel.recordEvent(new DuelEnded(duel.getId(), winnerIndex, loserIndex, reason));
    }

    @Override
    public void tick(Duel duel) {
        // Terminal phase — no-op
    }

    @Override
    public void onExit(Duel duel) {
    }
}
