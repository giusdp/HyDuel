package com.giusdp.htduels.duel;

import com.giusdp.htduels.FakeCardRepo;
import com.giusdp.htduels.duelist.BotTurnStrategy;
import com.giusdp.htduels.duelist.PlayerTurnStrategy;
import com.giusdp.htduels.duelist.Duelist;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DuelBuilderTest {

    @Test
    void assignsDuelId() {
        Duel duel = Duel.builder()
                .cardRepo(new FakeCardRepo())
                .addDuelist(new Duelist(new PlayerTurnStrategy()), true)
                .addDuelist(new Duelist(new BotTurnStrategy()), false)
                .build();

        assertNotNull(duel.getId());
        assertNotNull(duel.getId().value());
    }

    @Test
    void buildsWithTwoDuelists() {
        Duelist d1 = new Duelist(new PlayerTurnStrategy());
        Duelist d2 = new Duelist(new BotTurnStrategy());
        Duel duel = Duel.builder()
                .cardRepo(new FakeCardRepo())
                .addDuelist(d1, true)
                .addDuelist(d2, false)
                .build();

        assertSame(d1, duel.getDuelist(0));
        assertSame(d2, duel.getDuelist(1));
    }

    @Test
    void setsBottomPlayer() {
        Duelist d1 = new Duelist(new PlayerTurnStrategy());
        Duelist d2 = new Duelist(new BotTurnStrategy());
        Duel.builder()
                .cardRepo(new FakeCardRepo())
                .addDuelist(d1, true)
                .addDuelist(d2, false)
                .build();

        assertTrue(d1.isOpponentSide());
        assertFalse(d2.isOpponentSide());
    }

    @Test
    void throwsWithoutCardRepo() {
        var builder = Duel.builder()
                .addDuelist(new Duelist(new PlayerTurnStrategy()), true)
                .addDuelist(new Duelist(new BotTurnStrategy()), false);

        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void buildsWithZeroDuelists() {
        Duel duel = Duel.builder()
                .cardRepo(new FakeCardRepo())
                .build();

        assertEquals(0, duel.getDuelists().size());
    }

    @Test
    void buildsWithOneDuelist() {
        Duelist d1 = new Duelist(new PlayerTurnStrategy());
        Duel duel = Duel.builder()
                .cardRepo(new FakeCardRepo())
                .addDuelist(d1, true)
                .build();

        assertEquals(1, duel.getDuelists().size());
        assertSame(d1, duel.getDuelist(0));
    }
}
