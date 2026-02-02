package com.giusdp.htduels.duel.phases;

import com.giusdp.htduels.FakeCardRepo;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duelist.Bot;
import com.giusdp.htduels.duelist.DuelPlayer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WaitingPhaseTest {

    private Duel createDuelWithNoDuelists() {
        Duel duel = Duel.builder()
                .cardRepo(new FakeCardRepo())
                .build();
        duel.setup();
        return duel;
    }

    @Test
    void staysInWaitingPhaseWithZeroDuelists() {
        Duel duel = createDuelWithNoDuelists();
        duel.tick();
        assertTrue(duel.isInPhase(WaitingPhase.class));
    }

    @Test
    void staysInWaitingPhaseWithOneDuelist() {
        Duel duel = createDuelWithNoDuelists();
        duel.addDuelist(new DuelPlayer());
        duel.tick();
        assertTrue(duel.isInPhase(WaitingPhase.class));
    }

    @Test
    void transitionsToStartupImmediatelyWhenSecondDuelistJoins() {
        Duel duel = createDuelWithNoDuelists();
        duel.addDuelist(new DuelPlayer());
        assertTrue(duel.isInPhase(WaitingPhase.class));

        // Adding second duelist triggers transition immediately
        duel.addDuelist(new Bot());
        assertTrue(duel.isInPhase(StartupPhase.class));
    }

    @Test
    void timesOutAfterMaxWaitTicks() {
        Duel duel = createDuelWithNoDuelists();
        for (int i = 0; i < WaitingPhase.MAX_WAIT_TICKS; i++) {
            duel.tick();
            assertTrue(duel.isInPhase(WaitingPhase.class));
        }
        duel.tick();
        assertTrue(duel.isInPhase(DuelEndPhase.class));
        assertTrue(duel.isFinished());
    }
}
