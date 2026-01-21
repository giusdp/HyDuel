package com.giusdp.htduels.handlers;

import static org.junit.jupiter.api.Assertions.*;

import com.giusdp.htduels.FakeCardRepo;
import com.giusdp.htduels.FakeEventBus;
import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.event.PlayCard;
import com.giusdp.htduels.duelist.Bot;
import com.giusdp.htduels.duelist.DuelPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlayCardHandlerTest {
    CardAsset card;
    Duel duel;

    @BeforeEach
    void setup() {
        this.duel = new Duel(new DuelPlayer(), new Bot(), new FakeEventBus(), new FakeCardRepo());
        this.duel.setup();
        this.card = new CardAsset("test", "Test Card", 1, 2, 3, "Minion");
        this.duel.duelist1.addToHand(card);
    }

    @Test
    void playingCardMovesItFromHandToBattlefield() {
        assertTrue(duel.duelist1.getHand().contains(card));
        assertTrue(duel.battlefield.getSide(duel.duelist1).isEmpty());

        duel.emit(new PlayCard(duel.duelist1, card));

        assertFalse(duel.duelist1.getHand().contains(card));
        assertTrue(duel.battlefield.getSide(duel.duelist1).contains(card));
    }

    @Test
    void playingCardDoesNotAffectOtherPlayerSide() {
        duel.emit(new PlayCard(duel.duelist1, card));
        assertTrue(duel.battlefield.getSide(duel.duelist2).isEmpty());
    }
}
