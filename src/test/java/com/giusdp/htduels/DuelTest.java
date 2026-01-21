package com.giusdp.htduels;

import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.event.DuelEvent;
import com.giusdp.htduels.duel.event.DuelStarted;
import com.giusdp.htduels.duel.phases.StartupPhase;
import com.giusdp.htduels.duel.phases.TurnStartPhase;
import com.giusdp.htduels.duelist.Bot;
import com.giusdp.htduels.duelist.DuelPlayer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DuelTest {
    Duel newDuel() {
        var duel = new Duel(new DuelPlayer(), new Bot(), new FakeEventBus(), new FakeCardRepo());
        duel.setup();
        return duel;
    }

    @Test
    void newDuelStartsInStartupAndTransitionsToTurnStart() {
        Duel duel = newDuel();
        assertEquals(StartupPhase.class, duel.currentPhase.getClass());
        duel.tick();
        assertEquals(TurnStartPhase.class, duel.currentPhase.getClass());
    }

    @Test
    void handsGetFilledOnStartup() {
        Duel duel = newDuel();
        duel.tick();
        assertEquals(5, duel.duelist1.getHand().size());
        assertEquals(5, duel.duelist2.getHand().size());
    }

    @Test
    void duelEmitsDuelStartedMove() {
        FakeEventBus eventBus = new FakeEventBus();
        var duel = new Duel(new DuelPlayer(), new Bot(), eventBus, new FakeCardRepo());
        duel.setup();
        List<DuelEvent> moves = eventBus.postedEvents();
        assertFalse(moves.isEmpty());
        assertInstanceOf(DuelStarted.class, moves.getFirst());
    }
}
