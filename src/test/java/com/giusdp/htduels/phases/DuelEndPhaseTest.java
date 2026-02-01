package com.giusdp.htduels.phases;

import com.giusdp.htduels.FakeCardRepo;
import com.giusdp.htduels.FakeEventBus;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.phases.DuelEndPhase;
import com.giusdp.htduels.duel.phases.MainPhase;
import com.giusdp.htduels.duel.phases.WaitingPhase;
import com.giusdp.htduels.duelist.DuelPlayer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DuelEndPhaseTest {

    private Duel createDuel() {
        return Duel.builder()
                .eventBus(new FakeEventBus())
                .cardRepo(new FakeCardRepo())
                .addDuelist(new DuelPlayer(), true)
                .addDuelist(new DuelPlayer(), false)
                .build();
    }

    @Test
    void forfeitDuringMainPhase() {
        Duel duel = createDuel();
        duel.setup();

        // Tick through WaitingPhase (1) + StartupPhase (10 draws) + transition tick (1)
        for (int i = 0; i < 12; i++) {
            duel.tick();
        }
        // Now in TurnStartPhase, one more tick transitions to MainPhase
        duel.tick();
        assertInstanceOf(MainPhase.class, duel.currentPhase);

        duel.transitionTo(new DuelEndPhase(DuelEndPhase.Reason.FORFEIT));
        assertInstanceOf(DuelEndPhase.class, duel.currentPhase);

        // Tick is a no-op — phase stays the same
        duel.tick();
        assertInstanceOf(DuelEndPhase.class, duel.currentPhase);
    }

    @Test
    void forfeitDuringStartup() {
        Duel duel = createDuel();
        duel.setup();
        assertInstanceOf(WaitingPhase.class, duel.currentPhase);

        duel.transitionTo(new DuelEndPhase(DuelEndPhase.Reason.FORFEIT));
        assertInstanceOf(DuelEndPhase.class, duel.currentPhase);
    }

    @Test
    void reasonIsPreserved() {
        Duel duel = createDuel();
        duel.setup();

        duel.transitionTo(new DuelEndPhase(DuelEndPhase.Reason.FORFEIT));
        assertEquals(DuelEndPhase.Reason.FORFEIT, ((DuelEndPhase) duel.currentPhase).reason);

        duel = createDuel();
        duel.setup();
        duel.transitionTo(new DuelEndPhase(DuelEndPhase.Reason.WIN));
        assertEquals(DuelEndPhase.Reason.WIN, ((DuelEndPhase) duel.currentPhase).reason);

        duel = createDuel();
        duel.setup();
        duel.transitionTo(new DuelEndPhase(DuelEndPhase.Reason.TIMEOUT));
        assertEquals(DuelEndPhase.Reason.TIMEOUT, ((DuelEndPhase) duel.currentPhase).reason);
    }
}
