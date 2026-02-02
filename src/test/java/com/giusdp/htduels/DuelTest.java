package com.giusdp.htduels;

import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.phases.StartupPhase;
import com.giusdp.htduels.duel.phases.TurnStartPhase;
import com.giusdp.htduels.duel.phases.WaitingPhase;
import com.giusdp.htduels.duelist.Bot;
import com.giusdp.htduels.duelist.DuelPlayer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DuelTest {

    @Test
    void setupWithTwoDuelistsSkipsWaitingPhase() {
        Duel duel = Duel.builder()
                .cardRepo(new FakeCardRepo())
                .addDuelist(new DuelPlayer(), false)
                .addDuelist(new Bot(), true)
                .build();
        duel.setup();
        assertTrue(duel.isInPhase(StartupPhase.class));
    }

    @Test
    void setupWithZeroDuelistsStaysInWaitingPhase() {
        Duel duel = Duel.builder()
                .cardRepo(new FakeCardRepo())
                .build();
        duel.setup();
        assertTrue(duel.isInPhase(WaitingPhase.class));
    }

    @Test
    void addingSecondDuelistTransitionsToStartup() {
        Duel duel = Duel.builder()
                .cardRepo(new FakeCardRepo())
                .build();
        duel.setup();
        duel.addDuelist(new DuelPlayer());
        assertTrue(duel.isInPhase(WaitingPhase.class));

        duel.addDuelist(new Bot());
        assertTrue(duel.isInPhase(StartupPhase.class));
    }

    @Test
    void fullFlowToTurnStart() {
        Duel duel = Duel.builder()
                .cardRepo(new FakeCardRepo())
                .addDuelist(new DuelPlayer(), false)
                .addDuelist(new Bot(), true)
                .build();
        duel.setup();

        // 10 ticks in StartupPhase (draw cards)
        for (int i = 0; i < 10; i++) {
            duel.tick();
            assertTrue(duel.isInPhase(StartupPhase.class));
        }
        duel.tick();
        assertTrue(duel.isInPhase(TurnStartPhase.class));
    }

    @Test
    void handsGetFilledOnStartup() {
        Duel duel = Duel.builder()
                .cardRepo(new FakeCardRepo())
                .addDuelist(new DuelPlayer(), false)
                .addDuelist(new Bot(), true)
                .build();
        duel.setup();

        for (int i = 0; i < 10; i++) {
            duel.tick();
        }
        assertEquals(5, duel.getDuelist(0).getHand().getCards().size());
        assertEquals(5, duel.getDuelist(1).getHand().getCards().size());
    }
}
