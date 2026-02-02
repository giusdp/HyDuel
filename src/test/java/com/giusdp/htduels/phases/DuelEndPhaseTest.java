package com.giusdp.htduels.phases;

import com.giusdp.htduels.FakeCardRepo;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.phases.DuelEndPhase;
import com.giusdp.htduels.duel.phases.MainPhase;
import com.giusdp.htduels.duel.phases.StartupPhase;
import com.giusdp.htduels.duelist.PlayerTurnStrategy;
import com.giusdp.htduels.duelist.Duelist;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DuelEndPhaseTest {

    private Duel createDuel() {
        return Duel.builder()
                .cardRepo(new FakeCardRepo())
                .addDuelist(new Duelist(new PlayerTurnStrategy()), true)
                .addDuelist(new Duelist(new PlayerTurnStrategy()), false)
                .build();
    }

    @Test
    void forfeitDuringMainPhase() {
        Duel duel = createDuel();
        duel.setup();

        // Tick through StartupPhase (10 draws) + transition tick
        for (int i = 0; i < 11; i++) {
            duel.tick();
        }
        // Now in TurnStartPhase, one more tick transitions to MainPhase
        duel.tick();
        assertTrue(duel.isInPhase(MainPhase.class));

        duel.forfeit();
        assertTrue(duel.isInPhase(DuelEndPhase.class));

        // Tick is a no-op — phase stays the same
        duel.tick();
        assertTrue(duel.isInPhase(DuelEndPhase.class));
    }

    @Test
    void forfeitDuringStartup() {
        Duel duel = createDuel();
        duel.setup();
        assertTrue(duel.isInPhase(StartupPhase.class));

        duel.forfeit();
        assertTrue(duel.isInPhase(DuelEndPhase.class));
    }

    @Test
    void forfeitReasonIsPreserved() {
        Duel duel = createDuel();
        duel.setup();

        duel.forfeit();
        assertEquals(DuelEndPhase.Reason.FORFEIT, duel.getEndReason());
    }

    @Test
    void timeoutReasonIsPreserved() {
        Duel duel = Duel.builder()
                .cardRepo(new FakeCardRepo())
                .build();
        duel.setup();

        duel.transitionTo(new DuelEndPhase(DuelEndPhase.Reason.TIMEOUT));
        assertEquals(DuelEndPhase.Reason.TIMEOUT, duel.getEndReason());
    }

    @Test
    void winReasonIsPreserved() {
        Duel duel = createDuel();
        duel.setup();

        duel.transitionTo(new DuelEndPhase(DuelEndPhase.Reason.WIN));
        assertEquals(DuelEndPhase.Reason.WIN, duel.getEndReason());
    }
}
