package com.giusdp.htduels.duel;

import com.giusdp.htduels.duel.phases.StartupPhase;
import com.giusdp.htduels.duel.phases.TurnStartPhase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DuelTest {

    @Test
    void newDuelStartsInStartupAndTransitionsToTurnStart() {
        Duel duel = new Duel();

        assertEquals(StartupPhase.class, duel.getCurrentPhase().getClass());

        duel.tick();

        assertEquals(TurnStartPhase.class, duel.getCurrentPhase().getClass());
    }
}
