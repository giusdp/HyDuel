package com.giusdp.htduels.handlers;

import com.giusdp.htduels.FakeCardRepo;
import com.giusdp.htduels.FakeEventBus;
import com.giusdp.htduels.TestBoardLayout;
import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.duel.Card;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.event.PlayCard;
import com.giusdp.htduels.duelist.Bot;
import com.giusdp.htduels.duelist.DuelPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayCardHandlerTest {
    Card card;
    Duel duel;

    @BeforeEach
    void setup() {
        this.duel = new Duel(new DuelPlayer(), new Bot(), new FakeEventBus(), new FakeCardRepo());
        this.duel.setup();
        // Tick through all 10 startup draws
        for (int i = 0; i < 10; i++) {
            this.duel.tick();
        }
        this.card = new Card(new CardAsset("test", "Test Card", 1, 2, 3, "Minion"));
        this.duel.duelist1.addToHand(card);
    }

    @Test
    void playingCardMovesItFromHandToBattlefield() {
        assertTrue(duel.duelist1.getHand().getCards().contains(card));
        assertTrue(duel.duelist1.getBattlefield().getCards().isEmpty());

        duel.emit(new PlayCard(duel, duel.duelist1, card));

        assertFalse(duel.duelist1.getHand().getCards().contains(card));
        assertTrue(duel.duelist1.getBattlefield().getCards().contains(card));
    }

    @Test
    void playingCardDoesNotAffectOtherPlayerSide() {
        duel.emit(new PlayCard(duel, duel.duelist1, card));
        assertTrue(duel.duelist2.getBattlefield().getCards().isEmpty());
        assertEquals(5, duel.duelist2.getHand().getCards().size());
    }
}
