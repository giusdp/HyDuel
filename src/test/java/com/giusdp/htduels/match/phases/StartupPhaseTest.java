package com.giusdp.htduels.match.phases;

import com.giusdp.htduels.FakeCardRepo;
import com.giusdp.htduels.match.Duel;
import com.giusdp.htduels.match.event.CardsDrawn;
import com.giusdp.htduels.match.event.DuelEvent;
import com.giusdp.htduels.match.event.DuelStarted;
import com.giusdp.htduels.match.event.StartingDuelistSelected;
import com.giusdp.htduels.match.phases.StartupPhase;
import com.giusdp.htduels.match.phases.TurnStartPhase;
import com.giusdp.htduels.match.HumanTurnStrategy;
import com.giusdp.htduels.match.Duelist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StartupPhaseTest {
    Duel duel;

    @BeforeEach
    void setup() {
        duel = Duel.builder()
                .cardRepo(new FakeCardRepo())
                .addDuelist(new Duelist(new HumanTurnStrategy()), true)
                .addDuelist(new Duelist(new HumanTurnStrategy()), false)
                .build();
        duel.setup();
        assertTrue(duel.isInPhase(StartupPhase.class));
    }

    @Test
    void onEnterRecordsDuelStartedAndRandomDuelistSelect() {
        List<DuelEvent> events = duel.getAccumulatedEvents();
        assertInstanceOf(DuelStarted.class, events.get(0));
        assertInstanceOf(StartingDuelistSelected.class, events.get(1));
        assertEquals(2, events.size());
    }

    @Test
    void afterOneTickRecordsOneDrawCards() {
        duel.tick();
        long drawCount = duel.getAccumulatedEvents().stream().filter(e -> e instanceof CardsDrawn).count();
        assertEquals(1, drawCount);
        assertEquals(1, duel.getDuelist(0).getHand().getCards().size());
    }

    @Test
    void afterFiveTicksDuelist1HasFiveCardsDuelist2HasZero() {
        for (int i = 0; i < 5; i++) {
            duel.tick();
        }
        long drawCount = duel.getAccumulatedEvents().stream().filter(e -> e instanceof CardsDrawn).count();
        assertEquals(5, drawCount);
        assertEquals(5, duel.getDuelist(0).getHand().getCards().size());
        assertEquals(0, duel.getDuelist(1).getHand().getCards().size());
    }

    @Test
    void afterTenTicksBothHaveFiveCards() {
        for (int i = 0; i < 10; i++) {
            duel.tick();
        }
        long drawCount = duel.getAccumulatedEvents().stream().filter(e -> e instanceof CardsDrawn).count();
        assertEquals(10, drawCount);
        assertEquals(5, duel.getDuelist(0).getHand().getCards().size());
        assertEquals(5, duel.getDuelist(1).getHand().getCards().size());
    }

    @Test
    void staysInStartupPhaseDuringDraws() {
        for (int i = 0; i < 10; i++) {
            duel.tick();
            assertTrue(duel.isInPhase(StartupPhase.class));
        }
    }

    @Test
    void transitionsToTurnStartPhaseAfterAllDraws() {
        for (int i = 0; i < 10; i++) {
            duel.tick();
        }
        duel.tick();
        assertTrue(duel.isInPhase(TurnStartPhase.class));
    }
}
