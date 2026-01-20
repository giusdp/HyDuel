package com.giusdp.htduels.phases;

import com.giusdp.htduels.FakeCardRepo;
import com.giusdp.htduels.FakeEventBus;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.event.DrawCards;
import com.giusdp.htduels.duel.event.DuelEvent;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StartupPhaseTest {

    @Test
    void emitsDrawCardsMoves() {
        FakeEventBus eventBus = new FakeEventBus();
        new Duel(eventBus, new FakeCardRepo());
        List<DuelEvent> moves = eventBus.postedEvents();

        long drawCardsCount = moves.stream()
                .filter(m -> m instanceof DrawCards)
                .count();
        assertEquals(2, drawCardsCount);
    }
}
