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

class CardHoveredTest {
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
        CardHovered event = new CardHovered(duel, card, duel.duelist1);

        assertEquals(duel, event.duel);
    }

    @Test
    void eventContainsHoveredCard() {
        CardHovered event = new CardHovered(duel, card, duel.duelist1);

        assertEquals(card, event.card);
    }

    @Test
    void eventContainsViewer() {
        Duelist viewer = duel.duelist1;
        CardHovered event = new CardHovered(duel, card, viewer);

        assertEquals(viewer, event.viewer);
    }

    @Test
    void canHoverOpponentCard() {
        Card opponentCard = duel.duelist2.getHand().getCards().getFirst();
        CardHovered event = new CardHovered(duel, opponentCard, duel.duelist1);

        assertEquals(opponentCard, event.card);
        assertEquals(duel.duelist1, event.viewer);
    }
}
