package com.giusdp.htduels.phases;

import com.giusdp.htduels.FakeCardRepo;
import com.giusdp.htduels.FakeEventBus;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.event.DrawCards;
import com.giusdp.htduels.duel.event.DuelEvent;

import com.giusdp.htduels.duel.event.RandomDuelistSelect;
import com.giusdp.htduels.duelist.Bot;
import com.giusdp.htduels.duelist.DuelPlayer;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class StartupPhaseTest {
    FakeEventBus setup() {
        FakeEventBus eventBus = new FakeEventBus();
        var duel = new Duel(new DuelPlayer(), new DuelPlayer(), eventBus, new FakeCardRepo());
        duel.setup();
        return eventBus;
    }

    @Test
    void emitsDrawCardsMoves() {
        FakeEventBus eventBus = setup();
        List<DuelEvent> moves = eventBus.postedEvents();
        long drawCardsCount = moves.stream()
                .filter(m -> m instanceof DrawCards)
                .count();
        assertEquals(2, drawCardsCount);
    }

    @Test
    void startupPhaseEmitsRandomDuelistSelect() {
        FakeEventBus eventBus = setup();
        var moves = eventBus.postedEvents().stream()
                .filter(e -> e instanceof RandomDuelistSelect)
                .toList();
        assertFalse(moves.isEmpty());
    }
}
