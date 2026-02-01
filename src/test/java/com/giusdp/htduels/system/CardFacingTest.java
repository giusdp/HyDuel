package com.giusdp.htduels.system;

import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.duel.Card;
import com.giusdp.htduels.duelist.DuelPlayer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardFacingTest {

    private Card createCard() {
        return new Card(new CardAsset("test", "Test", 1, 1, 1, "Minion"));
    }

    @Test
    void topPlayerHandCardIsFaceUp() {
        DuelPlayer player = new DuelPlayer();
        player.setOpponentSide(false);
        Card card = createCard();
        player.addToHand(card);

        assertEquals(0f, CardSpatialResolutionSystem.resolveFacing(card), 0.001);
    }

    @Test
    void bottomPlayerHandCardIsFaceDown() {
        DuelPlayer player = new DuelPlayer();
        player.setOpponentSide(true);
        Card card = createCard();
        player.addToHand(card);

        assertEquals((float) Math.PI, CardSpatialResolutionSystem.resolveFacing(card), 0.001);
    }

    @Test
    void topPlayerBattlefieldCardIsFaceUp() {
        DuelPlayer player = new DuelPlayer();
        player.setOpponentSide(false);
        Card card = createCard();
        player.playCard(card);

        assertEquals(0f, CardSpatialResolutionSystem.resolveFacing(card), 0.001);
    }

    @Test
    void bottomPlayerBattlefieldCardIsFaceUp() {
        DuelPlayer player = new DuelPlayer();
        player.setOpponentSide(true);
        Card card = createCard();
        player.playCard(card);

        assertEquals(0f, CardSpatialResolutionSystem.resolveFacing(card), 0.001);
    }
}
