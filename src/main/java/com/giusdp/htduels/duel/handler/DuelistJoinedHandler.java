package com.giusdp.htduels.duel.handler;

import com.giusdp.htduels.duel.event.DuelEvent;
import com.giusdp.htduels.duel.phases.StartupPhase;
import com.giusdp.htduels.duel.phases.WaitingPhase;

public class DuelistJoinedHandler extends DuelEventHandler {

    @Override
    public void accept(DuelEvent ev) {
        if (ev.duel.currentPhase instanceof WaitingPhase && ev.duel.getDuelists().size() >= 2) {
            ev.duel.transitionTo(new StartupPhase());
        }
    }
}
