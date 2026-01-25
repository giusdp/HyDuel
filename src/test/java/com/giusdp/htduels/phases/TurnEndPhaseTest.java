package com.giusdp.htduels.phases;

import com.giusdp.htduels.FakeCardRepo;
import com.giusdp.htduels.FakeEventBus;
import com.giusdp.htduels.TestBoardLayout;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.event.EndMainPhase;
import com.giusdp.htduels.duelist.Bot;
import com.giusdp.htduels.duelist.DuelPlayer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TurnEndPhaseTest {

    @Test
    void transitionsToTurnStartFromEndTurnSwappingDuelists() {
        var duel = new Duel(new DuelPlayer(), new Bot(), new FakeEventBus(), new FakeCardRepo());

        duel.setup();
        duel.tick(); // Move to TurnStartPhase
        var initialDuelist = duel.activeDuelist;

        duel.tick(); // Move to MainPhase
        duel.emit(new EndMainPhase(duel));
        duel.tick(); // Move to TurnEndPhase

        var newDuelist = duel.activeDuelist;
        assertNotEquals(initialDuelist, newDuelist);
    }
}
