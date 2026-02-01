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
        var duel = Duel.builder()
                .eventBus(new FakeEventBus())
                .cardRepo(new FakeCardRepo())
                .addDuelist(new DuelPlayer(), true)
                .addDuelist(new Bot(), false)
                .build();

        duel.setup();
        // WaitingPhase -> StartupPhase
        duel.tick();
        // StartupPhase draws (10 ticks) + transition to TurnStartPhase
        for (int i = 0; i < 10; i++) {
            duel.tick();
        }
        duel.tick(); // Move to TurnStartPhase
        var initialDuelist = duel.activeDuelist;

        duel.tick(); // Move to MainPhase
        duel.emit(new EndMainPhase(duel));
        duel.tick(); // Move to TurnEndPhase

        var newDuelist = duel.activeDuelist;
        assertNotEquals(initialDuelist, newDuelist);
    }
}
