package com.giusdp.htduels.duel.handlers;

import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.event.DuelEvent;
import com.giusdp.htduels.duel.phases.TurnEndPhase;
import org.jspecify.annotations.NonNull;

public class EndMainPhaseHandler extends DuelEventHandler {
    public EndMainPhaseHandler(@NonNull Duel duel) {
        super(duel);
    }

    @Override
    public void accept(DuelEvent duelEvent) {
        duel.transitionTo(new TurnEndPhase());
    }
}
