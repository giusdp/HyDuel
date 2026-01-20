package com.giusdp.htduels.duel.card;

import com.giusdp.htduels.asset.CardAsset;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardAssetTest {

    @Test
    void cardAssetHoldsAllFields() {
        CardAsset card = new CardAsset("Wisp", "Wisp", 0, 1, 1, "Minion");

        assertEquals("Wisp", card.id());
        assertEquals("Wisp", card.name());
        assertEquals(0, card.cost());
        assertEquals(1, card.attack());
        assertEquals(1, card.health());
        assertEquals("Minion", card.type());
    }

    @Test
    void getIdReturnsId() {
        CardAsset card = new CardAsset("Fireball", "Fireball", 4, 0, 0, "Spell");

        assertEquals("Fireball", card.getId());
    }

    @Test
    void twoCardsWithSameFieldsAreEqual() {
        CardAsset card1 = new CardAsset("Wisp", "Wisp", 0, 1, 1, "Minion");
        CardAsset card2 = new CardAsset("Wisp", "Wisp", 0, 1, 1, "Minion");

        assertEquals(card1, card2);
    }
}
