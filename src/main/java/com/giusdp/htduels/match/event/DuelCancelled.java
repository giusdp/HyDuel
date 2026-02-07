package com.giusdp.htduels.match.event;

import com.giusdp.htduels.match.DuelId;
import org.jspecify.annotations.NonNull;

public class DuelCancelled extends DuelEvent {

    public enum Reason {
        NO_OPPONENT
    }

    public final Reason reason;

    public DuelCancelled(@NonNull DuelId duelId, Reason reason) {
        super(duelId);
        this.reason = reason;
    }
}
