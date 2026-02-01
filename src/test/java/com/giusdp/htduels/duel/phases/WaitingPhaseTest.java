package com.giusdp.htduels.duel.phases;

import com.giusdp.htduels.FakeCardRepo;
import com.giusdp.htduels.FakeEventBus;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duelist.Bot;
import com.giusdp.htduels.duelist.DuelPlayer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void transitionsToStartupImmediatelyWhenSecondDuelistJoins() {
        Duel duel = createDuelWithNoDuelists();
        duel.addDuelist(new DuelPlayer());
        assertInstanceOf(WaitingPhase.class, duel.currentPhase);

        // Adding second duelist triggers DuelistJoined event -> handler transitions immediately
        duel.addDuelist(new Bot());
        assertInstanceOf(StartupPhase.class, duel.currentPhase);
    }

    @Test
    void timesOutAfterMaxWaitTicks() {
        Duel duel = createDuelWithNoDuelists();
        for (int i = 0; i < WaitingPhase.MAX_WAIT_TICKS; i++) {
            duel.tick();
            assertInstanceOf(WaitingPhase.class, duel.currentPhase);
        }
        duel.tick();
        assertInstanceOf(DuelEndPhase.class, duel.currentPhase);
        assertEquals(DuelEndPhase.Reason.TIMEOUT, ((DuelEndPhase) duel.currentPhase).reason);
    }
}
