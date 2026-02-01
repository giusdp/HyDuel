package com.giusdp.htduels.duel.phases;

import com.giusdp.htduels.FakeCardRepo;
import com.giusdp.htduels.FakeEventBus;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duelist.Bot;
import com.giusdp.htduels.duelist.DuelPlayer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class WaitingPhaseTest {

    private Duel createDuelWithNoDuelists() {
        Duel duel = Duel.builder()
                .eventBus(new FakeEventBus())
                .cardRepo(new FakeCardRepo())
                .build();
        duel.setup();
        return duel;
    }

    @Test
    void staysInWaitingPhaseWithZeroDuelists() {
        Duel duel = createDuelWithNoDuelists();
        duel.tick();
        assertInstanceOf(WaitingPhase.class, duel.currentPhase);
    }

    @Test
    void staysInWaitingPhaseWithOneDuelist() {
        Duel duel = createDuelWithNoDuelists();
        duel.addDuelist(new DuelPlayer());
        duel.tick();
        assertInstanceOf(WaitingPhase.class, duel.currentPhase);
    }

    @Test
    void transitionsToStartupWhenSecondDuelistAdded() {
        Duel duel = createDuelWithNoDuelists();
        duel.addDuelist(new DuelPlayer());
        duel.tick();
        assertInstanceOf(WaitingPhase.class, duel.currentPhase);

        duel.addDuelist(new Bot());
        duel.tick();
        assertInstanceOf(StartupPhase.class, duel.currentPhase);
    }

    @Test
    void timesOutAfterMaxWaitTicks() {
        Duel duel = createDuelWithNoDuelists();
        for (int i = 0; i < WaitingPhase.MAX_WAIT_TICKS; i++) {
            duel.tick();
            assertInstanceOf(WaitingPhase.class, duel.currentPhase);
        }
        // One more tick after reaching max
        duel.tick();
        assertInstanceOf(DuelEndPhase.class, duel.currentPhase);
        DuelEndPhase endPhase = (DuelEndPhase) duel.currentPhase;
        assertInstanceOf(DuelEndPhase.class, endPhase);
        assert endPhase.reason == DuelEndPhase.Reason.TIMEOUT;
    }
}
