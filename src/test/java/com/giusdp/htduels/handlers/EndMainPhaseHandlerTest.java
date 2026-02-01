package com.giusdp.htduels.handlers;

import com.giusdp.htduels.FakeCardRepo;
import com.giusdp.htduels.FakeEventBus;
import com.giusdp.htduels.TestBoardLayout;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.event.EndMainPhase;
import com.giusdp.htduels.duel.phases.MainPhase;
import com.giusdp.htduels.duel.phases.TurnEndPhase;
import com.giusdp.htduels.duelist.Bot;
import com.giusdp.htduels.duelist.DuelPlayer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class EndMainPhaseHandlerTest {
    @Test
    void addsCardsToHand() {
        Duel duel = Duel.builder()
                .eventBus(new FakeEventBus())
                .cardRepo(new FakeCardRepo())
                .addDuelist(new DuelPlayer(), true)
                .addDuelist(new Bot(), false)
                .build();
        duel.setup();
        // 1 tick WaitingPhase, 10 ticks for startup draws, 1 to transition to TurnStart, 1 to transition to MainPhase
        for (int i = 0; i < 13; i++) {
            duel.tick();
        }

        assertInstanceOf(MainPhase.class, duel.currentPhase);

        duel.emit(new EndMainPhase(duel));
        assertInstanceOf(TurnEndPhase.class, duel.currentPhase);
    }
}
