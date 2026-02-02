package com.giusdp.htduels.phases;

import com.giusdp.htduels.FakeCardRepo;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duelist.Bot;
import com.giusdp.htduels.duelist.DuelPlayer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TurnEndPhaseTest {

    @Test
    void transitionsToTurnStartFromEndTurnSwappingDuelists() {
        var duel = Duel.builder()
                .cardRepo(new FakeCardRepo())
                .addDuelist(new DuelPlayer(), true)
                .addDuelist(new Bot(), false)
                .build();

        duel.setup();
        // StartupPhase draws (10 ticks) + transition to TurnStartPhase
        for (int i = 0; i < 10; i++) {
            duel.tick();
        }
        duel.tick(); // Move to TurnStartPhase
        var initialDuelist = duel.getActiveDuelist();

        duel.tick(); // Move to MainPhase
        duel.endMainPhase();
        duel.tick(); // Move to TurnEndPhase

        var newDuelist = duel.getActiveDuelist();
        assertNotEquals(initialDuelist, newDuelist);
    }
}
