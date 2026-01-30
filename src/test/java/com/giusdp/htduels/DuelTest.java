package com.giusdp.htduels;

import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.phases.StartupPhase;
import com.giusdp.htduels.duel.phases.TurnStartPhase;
import com.giusdp.htduels.duelist.Bot;
import com.giusdp.htduels.duelist.DuelPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DuelTest {
    Duel duel;

    @BeforeEach
    void setup() {
        duel = new Duel(new DuelPlayer(), new Bot(), new FakeEventBus(), new FakeCardRepo());
        duel.setup();
    }

    @Test
    void newDuelStartsInStartupAndTransitionsToTurnStart() {
        assertEquals(StartupPhase.class, duel.currentPhase.getClass());
        for (int i = 0; i < 10; i++) {
            duel.tick();
            assertEquals(StartupPhase.class, duel.currentPhase.getClass());
        }
        duel.tick();
        assertEquals(TurnStartPhase.class, duel.currentPhase.getClass());
    }

    @Test
    void handsGetFilledOnStartup() {
        for (int i = 0; i < 10; i++) {
            duel.tick();
        }
        assertEquals(5, duel.duelist1.getHand().getCards().size());
        assertEquals(5, duel.duelist2.getHand().getCards().size());
    }
}
