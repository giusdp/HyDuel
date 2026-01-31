package com.giusdp.htduels.duel;

import com.giusdp.htduels.FakeCardRepo;
import com.giusdp.htduels.FakeEventBus;
import com.giusdp.htduels.duelist.Bot;
import com.giusdp.htduels.duelist.DuelPlayer;
import com.giusdp.htduels.duelist.Duelist;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DuelBuilderTest {

    @Test
    void buildsWithTwoDuelists() {
        Duelist d1 = new DuelPlayer();
        Duelist d2 = new Bot();
        Duel duel = Duel.builder()
                .eventBus(new FakeEventBus())
                .cardRepo(new FakeCardRepo())
                .addDuelist(d1, true)
                .addDuelist(d2, false)
                .build();

        assertSame(d1, duel.getDuelist(0));
        assertSame(d2, duel.getDuelist(1));
    }

    @Test
    void setsBottomPlayer() {
        Duelist d1 = new DuelPlayer();
        Duelist d2 = new Bot();
        Duel.builder()
                .eventBus(new FakeEventBus())
                .cardRepo(new FakeCardRepo())
                .addDuelist(d1, true)
                .addDuelist(d2, false)
                .build();

        assertTrue(d1.isBottomPlayer());
        assertFalse(d2.isBottomPlayer());
    }

    @Test
    void wiresEventBusAndCardRepo() {
        var eventBus = new FakeEventBus();
        var cardRepo = new FakeCardRepo();
        Duel duel = Duel.builder()
                .eventBus(eventBus)
                .cardRepo(cardRepo)
                .addDuelist(new DuelPlayer(), true)
                .addDuelist(new Bot(), false)
                .build();

        assertSame(eventBus, duel.eventBus);
        assertSame(cardRepo, duel.cardRepo);
    }

    @Test
    void throwsWithoutEventBus() {
        var builder = Duel.builder()
                .cardRepo(new FakeCardRepo())
                .addDuelist(new DuelPlayer(), true)
                .addDuelist(new Bot(), false);

        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void throwsWithoutCardRepo() {
        var builder = Duel.builder()
                .eventBus(new FakeEventBus())
                .addDuelist(new DuelPlayer(), true)
                .addDuelist(new Bot(), false);

        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void throwsWithOneDuelist() {
        var builder = Duel.builder()
                .eventBus(new FakeEventBus())
                .cardRepo(new FakeCardRepo())
                .addDuelist(new DuelPlayer(), true);

        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void throwsWithThreeDuelists() {
        var builder = Duel.builder()
                .eventBus(new FakeEventBus())
                .cardRepo(new FakeCardRepo())
                .addDuelist(new DuelPlayer(), true)
                .addDuelist(new Bot(), false)
                .addDuelist(new DuelPlayer(), true);

        assertThrows(IllegalStateException.class, builder::build);
    }
}
