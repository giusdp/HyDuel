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
        this.duel.getDuelist(0).addToHand(card);
    }

    @Test
    void playingCardMovesItFromHandToBattlefield() {
        assertTrue(duel.getDuelist(0).getHand().getCards().contains(card));
        assertTrue(duel.getDuelist(0).getBattlefield().getCards().isEmpty());

        duel.emit(new PlayCard(duel, duel.getDuelist(0), card));

        assertFalse(duel.getDuelist(0).getHand().getCards().contains(card));
        assertTrue(duel.getDuelist(0).getBattlefield().getCards().contains(card));
    }

    @Test
    void playingCardDoesNotAffectOtherPlayerSide() {
        duel.emit(new PlayCard(duel, duel.getDuelist(0), card));
        assertTrue(duel.getDuelist(1).getBattlefield().getCards().isEmpty());
        assertEquals(5, duel.getDuelist(1).getHand().getCards().size());
    }
}
