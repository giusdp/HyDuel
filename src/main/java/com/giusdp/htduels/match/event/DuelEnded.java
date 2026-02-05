package com.giusdp.htduels.match.event;

import com.giusdp.htduels.match.DuelId;
import com.giusdp.htduels.match.phases.DuelEndPhase;
import org.jspecify.annotations.NonNull;

public class DuelEnded extends DuelEvent {
    public final int winnerIndex;
    public final int loserIndex;
    public final DuelEndPhase.Reason reason;

    public DuelEnded(@NonNull DuelId duelId, int winnerIndex, int loserIndex, DuelEndPhase.Reason reason) {
        super(duelId);
        this.winnerIndex = winnerIndex;
        this.loserIndex = loserIndex;
        this.reason = reason;
    }
}
