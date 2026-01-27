package com.giusdp.htduels.duel.event;

import com.giusdp.htduels.FakeCardRepo;
import com.giusdp.htduels.FakeEventBus;
import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.duel.Card;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duelist.Bot;
import com.giusdp.htduels.duelist.DuelPlayer;
import com.giusdp.htduels.duelist.Duelist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardUnhoveredTest {
    Duel duel;
    Card card;
    FakeEventBus eventBus;

    @BeforeEach
    void setup() {
        eventBus = new FakeEventBus();
        duel = new Duel(new DuelPlayer(), new Bot(), eventBus, new FakeCardRepo());
        duel.setup();
        card = new Card(new CardAsset("test", "Test Card", 1, 2, 3, "Minion"));
        duel.duelist1.addToHand(card);
    }

    @Test
    void eventContainsDuel() {
        CardUnhovered event = new CardUnhovered(duel, card, duel.duelist1);

        assertEquals(duel, event.duel);
    }

    @Test
    void eventContainsUnhoveredCard() {
        CardUnhovered event = new CardUnhovered(duel, card, duel.duelist1);

        assertEquals(card, event.card);
    }

    @Test
    void eventContainsViewer() {
        Duelist viewer = duel.duelist1;
        CardUnhovered event = new CardUnhovered(duel, card, viewer);

        assertEquals(viewer, event.viewer);
    }

    @Test
    void viewerCanBeDifferentDuelist() {
        CardUnhovered event1 = new CardUnhovered(duel, card, duel.duelist1);
        CardUnhovered event2 = new CardUnhovered(duel, card, duel.duelist2);

        assertEquals(duel.duelist1, event1.viewer);
        assertEquals(duel.duelist2, event2.viewer);
    }

    @Test
    void hoverAndUnhoverSequence() {
        int eventsBefore = eventBus.postedEvents().size();

        duel.emit(new CardHovered(duel, card, duel.duelist1));
        duel.emit(new CardUnhovered(duel, card, duel.duelist1));

        assertEquals(eventsBefore + 2, eventBus.postedEvents().size());
        assertInstanceOf(CardHovered.class, eventBus.postedEvents().get(eventsBefore));
        assertInstanceOf(CardUnhovered.class, eventBus.postedEvents().get(eventsBefore + 1));
    }
}
