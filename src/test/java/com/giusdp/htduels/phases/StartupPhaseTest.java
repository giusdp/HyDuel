package com.giusdp.htduels.phases;

import com.giusdp.htduels.FakeCardRepo;
import com.giusdp.htduels.FakeEventBus;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.event.DrawCards;
import com.giusdp.htduels.duel.event.DuelEvent;
import com.giusdp.htduels.duel.event.DuelStarted;
import com.giusdp.htduels.duel.event.RandomDuelistSelect;
import com.giusdp.htduels.duel.phases.StartupPhase;
import com.giusdp.htduels.duel.phases.TurnStartPhase;
import com.giusdp.htduels.duelist.DuelPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StartupPhaseTest {
    FakeEventBus eventBus;
    Duel duel;

    @BeforeEach
    void setup() {
        this.eventBus = new FakeEventBus();
        duel = Duel.builder()
                .eventBus(eventBus)
                .cardRepo(new FakeCardRepo())
                .addDuelist(new DuelPlayer(), true)
                .addDuelist(new DuelPlayer(), false)
                .build();
        duel.setup();
    }

    @Test
    void onEnterEmitsDuelStartedAndRandomDuelistSelectOnly() {
        List<DuelEvent> events = eventBus.postedEvents();
        assertInstanceOf(DuelStarted.class, events.get(0));
        assertInstanceOf(RandomDuelistSelect.class, events.get(1));
        assertEquals(2, events.size());
    }

    @Test
    void afterOneTickEmitsOneDrawCards() {
        duel.tick();
        long drawCount = eventBus.postedEvents().stream().filter(e -> e instanceof DrawCards).count();
        assertEquals(1, drawCount);
        assertEquals(1, duel.getDuelist(0).getHand().getCards().size());
    }

    @Test
    void afterFiveTicksDuelist1HasFiveCardsDuelist2HasZero() {
        for (int i = 0; i < 5; i++) {
            duel.tick();
        }
        long drawCount = eventBus.postedEvents().stream().filter(e -> e instanceof DrawCards).count();
        assertEquals(5, drawCount);
        assertEquals(5, duel.getDuelist(0).getHand().getCards().size());
        assertEquals(0, duel.getDuelist(1).getHand().getCards().size());
    }

    @Test
    void afterTenTicksBothHaveFiveCards() {
        for (int i = 0; i < 10; i++) {
            duel.tick();
        }
        long drawCount = eventBus.postedEvents().stream().filter(e -> e instanceof DrawCards).count();
        assertEquals(10, drawCount);
        assertEquals(5, duel.getDuelist(0).getHand().getCards().size());
        assertEquals(5, duel.getDuelist(1).getHand().getCards().size());
    }

    @Test
    void staysInStartupPhaseDuringDraws() {
        for (int i = 0; i < 10; i++) {
            duel.tick();
            assertInstanceOf(StartupPhase.class, duel.currentPhase);
        }
    }

    @Test
    void transitionsToTurnStartPhaseAfterAllDraws() {
        for (int i = 0; i < 10; i++) {
            duel.tick();
        }
        duel.tick();
        assertInstanceOf(TurnStartPhase.class, duel.currentPhase);
    }
}
