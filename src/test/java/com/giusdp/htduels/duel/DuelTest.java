package com.giusdp.htduels.duel;

import com.giusdp.htduels.duel.moves.DrawCards;
import com.giusdp.htduels.duel.moves.DuelStarted;
import com.giusdp.htduels.duel.moves.Move;
import com.giusdp.htduels.duel.phases.StartupPhase;
import com.giusdp.htduels.duel.phases.TurnStartPhase;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DuelTest {

    @Test
    void newDuelStartsInStartupAndTransitionsToTurnStart() {
        Duel duel = new Duel();
        assertEquals(StartupPhase.class, duel.currentPhase.getClass());
        duel.tick();
        assertEquals(TurnStartPhase.class, duel.currentPhase.getClass());
    }

    @Test
    void handsGetFilledOnStartup() {
        Duel duel = new Duel();
        duel.tick();
        assertNotEquals(0, duel.playerHands.length);
        assertEquals(5, duel.playerHands[0].cards.size());
        assertEquals(5, duel.playerHands[1].cards.size());
    }

    @Test
    void duelEmitsDuelStartedMove() {
        Duel duel = new Duel();
        List<Move> moves = duel.getMoves();

        assertFalse(moves.isEmpty());
        assertInstanceOf(DuelStarted.class, moves.getFirst());
    }

    @Test
    void drawCardsMoveAddsCardsToHand() {
        Duel duel = new Duel();
        duel.clearMoves();
        duel.playerHands[0].cards.clear();

        duel.emit(new DrawCards(0, 5));
        duel.tick();

        assertEquals(5, duel.playerHands[0].cards.size());
    }

    @Test
    void startupPhaseEmitsDrawCardsMoves() {
        Duel duel = new Duel();
        List<Move> moves = duel.getMoves();

        long drawCardsCount = moves.stream()
                .filter(m -> m instanceof DrawCards)
                .count();
        assertEquals(2, drawCardsCount);
    }
}
