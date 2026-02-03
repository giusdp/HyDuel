package com.giusdp.htduels.match.phases;

import com.giusdp.htduels.match.Duel;
import com.giusdp.htduels.match.DuelPhase;

public class DuelEndPhase extends DuelPhase {

    public enum Reason {
        FORFEIT, WIN, TIMEOUT
    }

    public final Reason reason;

    public DuelEndPhase(Reason reason) {
        this.reason = reason;
    }

    @Override
    public void onEnter(Duel duel) {
    }

    @Override
    public void tick(Duel duel) {
        // Terminal phase — no-op
    }

    @Override
    public void onExit(Duel duel) {
    }
}
