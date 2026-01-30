package com.giusdp.htduels.duel.phases;

import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.Phase;

public class DuelEndPhase extends Phase {

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
