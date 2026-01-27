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

class CardClickedTest {
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
        CardClicked event = new CardClicked(duel, card, duel.duelist1);
        assertEquals(duel, event.duel);
    }

    @Test
    void eventContainsClickedCard() {
        CardClicked event = new CardClicked(duel, card, duel.duelist1);
        assertEquals(card, event.card);
    }

    @Test
    void eventContainsClicker() {
        Duelist clicker = duel.duelist1;
        CardClicked event = new CardClicked(duel, card, clicker);
        assertEquals(clicker, event.clicker);
    }
}
