package com.giusdp.htduels.handlers;

import com.giusdp.htduels.FakeCardRepo;
import com.giusdp.htduels.FakeEventBus;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duelist.DuelPlayer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RandomDuelistSelectHandlerTest {
    @Test
    void picksADuelist() {
        var duelist1 = new DuelPlayer();
        var duelist2 = new DuelPlayer();
        Duel duel = new Duel(duelist1, duelist2, new FakeEventBus(), new FakeCardRepo());

        assertNull(duel.activeDuelist);
        duel.setup();
        assertTrue(duel.activeDuelist == duelist1 || duel.activeDuelist == duelist2);
    }
}
