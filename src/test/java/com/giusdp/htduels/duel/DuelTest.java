package com.giusdp.htduels.duel;

import com.giusdp.htduels.duel.phases.StartupPhase;
import com.giusdp.htduels.duel.phases.TurnStartPhase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DuelTest {

    @Test
    void newDuelStartsInStartupAndTransitionsToTurnStart() {
        Duel duel = new Duel();
        assertEquals(StartupPhase.class, duel.currentPhase.getClass());
        duel.tick();
        assertEquals(TurnStartPhase.class, duel.currentPhase.getClass());
    }

    @Test
    void handsGetFilledOnStartup() {
        Duel duel = new Duel();
        duel.tick();
        assertNotEquals(0, duel.playerHands.length);
        assertEquals(5, duel.playerHands[0].cards.size());
        assertEquals(5, duel.playerHands[1].cards.size());
    }
}
