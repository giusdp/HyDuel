package com.giusdp.htduels.match.phases;

import com.giusdp.htduels.match.Duel;
import com.giusdp.htduels.match.DuelPhase;
import com.giusdp.htduels.match.event.DuelCancelled;

public class DuelCancelledPhase extends DuelPhase {
    public final DuelCancelled.Reason reason;

    public DuelCancelledPhase(DuelCancelled.Reason reason) {
        this.reason = reason;
    }

    @Override
    public void onEnter(Duel duel) {
        duel.recordEvent(new DuelCancelled(duel.getId(), reason));
    }

    @Override
    public void tick(Duel duel) {
        // Terminal phase - no-op
    }

    @Override
    public void onExit(Duel duel) {
    }
}
