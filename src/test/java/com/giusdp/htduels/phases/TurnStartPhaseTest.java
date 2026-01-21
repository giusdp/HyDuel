package com.giusdp.htduels.phases;

import com.giusdp.htduels.FakeCardRepo;
import com.giusdp.htduels.FakeEventBus;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.event.DrawCards;
import com.giusdp.htduels.duel.event.DuelEvent;
import com.giusdp.htduels.duelist.DuelPlayer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TurnStartPhaseTest {

    @Test
    void emitsDrawCardsMoves() {
        FakeEventBus eventBus = new FakeEventBus();
        var duel = new Duel(new DuelPlayer(), new DuelPlayer(), eventBus, new FakeCardRepo());
        duel.setup();
        List<DuelEvent> moves = eventBus.postedEvents();
        duel.tick();
        long drawCardsCount = moves.stream().filter(m -> m instanceof DrawCards).count();
        assertEquals(3, drawCardsCount);
    }

}
