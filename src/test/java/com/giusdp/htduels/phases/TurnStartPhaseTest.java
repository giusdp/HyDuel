package com.giusdp.htduels.phases;

import com.giusdp.htduels.FakeCardRepo;
import com.giusdp.htduels.FakeEventBus;
import com.giusdp.htduels.TestBoardLayout;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.event.DrawCards;
import com.giusdp.htduels.duel.event.DuelEvent;
import com.giusdp.htduels.duel.phases.TurnStartPhase;
import com.giusdp.htduels.duelist.DuelPlayer;
import com.giusdp.htduels.duelist.Duelist;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TurnStartPhaseTest {

    @Test
    void turnIndicatorTextReturnsYourTurnForActiveDuelist() {
        Duelist active = new DuelPlayer();
        assertEquals("Your Turn", TurnStartPhase.turnIndicatorText(active, active));
    }

    @Test
    void turnIndicatorTextReturnsOpponentsTurnForInactiveDuelist() {
        Duelist player = new DuelPlayer();
        Duelist opponent = new DuelPlayer();
        assertEquals("Opponent's Turn", TurnStartPhase.turnIndicatorText(player, opponent));
    }

    @Test
    void emitsDrawCardsMoves() {
        FakeEventBus eventBus = new FakeEventBus();
        var duel = new Duel(new DuelPlayer(), new DuelPlayer(), eventBus, new FakeCardRepo());
        duel.setup();
        // Tick through 10 draws in StartupPhase
        for (int i = 0; i < 10; i++) {
            duel.tick();
        }
        // Tick 11 transitions to TurnStartPhase, which draws 1 more
        duel.tick();
        List<DuelEvent> moves = eventBus.postedEvents();
        long drawCardsCount = moves.stream().filter(m -> m instanceof DrawCards).count();
        assertEquals(11, drawCardsCount);
    }

}
