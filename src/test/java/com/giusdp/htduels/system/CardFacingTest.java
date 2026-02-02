package com.giusdp.htduels.system;

import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.duel.Card;
import com.giusdp.htduels.duelist.PlayerTurnStrategy;
import com.giusdp.htduels.duelist.Duelist;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardFacingTest {

    private Card createCard() {
        return new Card(new CardAsset("test", "Test", 1, 1, 1, "Minion"));
    }

    @Test
    void topPlayerHandCardIsFaceUp() {
        Duelist player = new Duelist(new PlayerTurnStrategy());
        player.setOpponentSide(false);
        Card card = createCard();
        player.addToHand(card);

        assertEquals(0f, CardSpatialResolutionSystem.resolveFacing(card), 0.001);
    }

    @Test
    void bottomPlayerHandCardIsFaceDown() {
        Duelist player = new Duelist(new PlayerTurnStrategy());
        player.setOpponentSide(true);
        Card card = createCard();
        player.addToHand(card);

        assertEquals((float) Math.PI, CardSpatialResolutionSystem.resolveFacing(card), 0.001);
    }

    @Test
    void topPlayerBattlefieldCardIsFaceUp() {
        Duelist player = new Duelist(new PlayerTurnStrategy());
        player.setOpponentSide(false);
        Card card = createCard();
        player.playCard(card);

        assertEquals(0f, CardSpatialResolutionSystem.resolveFacing(card), 0.001);
    }

    @Test
    void bottomPlayerBattlefieldCardIsFaceUp() {
        Duelist player = new Duelist(new PlayerTurnStrategy());
        player.setOpponentSide(true);
        Card card = createCard();
        player.playCard(card);

        assertEquals(0f, CardSpatialResolutionSystem.resolveFacing(card), 0.001);
    }
}
