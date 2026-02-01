package com.giusdp.htduels;

import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.phases.StartupPhase;
import com.giusdp.htduels.duel.phases.TurnStartPhase;
import com.giusdp.htduels.duel.phases.WaitingPhase;
import com.giusdp.htduels.duelist.Bot;
import com.giusdp.htduels.duelist.DuelPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class DuelTest {
    Duel duel;

    @BeforeEach
    void setup() {
        duel = Duel.builder()
                .eventBus(new FakeEventBus())
                .cardRepo(new FakeCardRepo())
                .addDuelist(new DuelPlayer(), false)
                .addDuelist(new Bot(), true)
                .build();
        duel.setup();
    }

    @Test
    void setupStartsInWaitingPhase() {
        assertInstanceOf(WaitingPhase.class, duel.currentPhase);
    }

    @Test
    void waitingPhaseTransitionsToStartupWhenTwoDuelists() {
        // Duel already has 2 duelists from builder, so first tick transitions
        assertInstanceOf(WaitingPhase.class, duel.currentPhase);
        duel.tick();
        assertInstanceOf(StartupPhase.class, duel.currentPhase);
    }

    @Test
    void fullFlowFromWaitingToTurnStart() {
        // First tick: WaitingPhase -> StartupPhase
        duel.tick();
        assertInstanceOf(StartupPhase.class, duel.currentPhase);

        // 10 ticks in StartupPhase (draw cards)
        for (int i = 0; i < 10; i++) {
            duel.tick();
            assertInstanceOf(StartupPhase.class, duel.currentPhase);
        }
        // 11th tick transitions to TurnStartPhase
        duel.tick();
        assertInstanceOf(TurnStartPhase.class, duel.currentPhase);
    }

    @Test
    void handsGetFilledOnStartup() {
        // WaitingPhase -> StartupPhase
        duel.tick();
        // 10 draw ticks
        for (int i = 0; i < 10; i++) {
            duel.tick();
        }
        assertEquals(5, duel.getDuelist(0).getHand().getCards().size());
        assertEquals(5, duel.getDuelist(1).getHand().getCards().size());
    }
}
