package com.giusdp.htduels;

import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.event.DrawCards;
import com.giusdp.htduels.duel.event.DuelEvent;
import com.giusdp.htduels.duel.event.DuelStarted;
import com.giusdp.htduels.duel.phases.StartupPhase;
import com.giusdp.htduels.duel.phases.TurnStartPhase;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DuelTest {


    @Test
    void newDuelStartsInStartupAndTransitionsToTurnStart() {
        Duel duel = new Duel(new FakeEventBus(), new FakeCardRepo());
        assertEquals(StartupPhase.class, duel.currentPhase.getClass());
        duel.tick();
        assertEquals(TurnStartPhase.class, duel.currentPhase.getClass());
    }

    @Test
    void handsGetFilledOnStartup() {
        Duel duel = new Duel(new FakeEventBus(), new FakeCardRepo());
        duel.tick();
        assertNotEquals(0, duel.playerHands.length);
        assertEquals(5, duel.playerHands[0].cards.size());
        assertEquals(5, duel.playerHands[1].cards.size());
    }

    @Test
    void duelEmitsDuelStartedMove() {
        FakeEventBus eventBus = new FakeEventBus();
        Duel duel = new Duel(eventBus, new FakeCardRepo());
        List<DuelEvent> moves = eventBus.postedEvents();
        assertFalse(moves.isEmpty());
        assertInstanceOf(DuelStarted.class, moves.getFirst());
    }

    @Test
    void drawCardsMoveAddsCardsToHand() {
        Duel duel = new Duel(new FakeEventBus(), new FakeCardRepo());
        duel.playerHands[0].cards.clear();
        duel.emit(new DrawCards(0, 5));
        duel.tick();

        assertEquals(5, duel.playerHands[0].cards.size());
    }

    @Test
    void startupPhaseEmitsDrawCardsMoves() {
        FakeEventBus eventBus = new FakeEventBus();
        new Duel(eventBus, new FakeCardRepo());
        List<DuelEvent> moves = eventBus.postedEvents();

        long drawCardsCount = moves.stream()
                .filter(m -> m instanceof DrawCards)
                .count();
        assertEquals(2, drawCardsCount);
    }
}
