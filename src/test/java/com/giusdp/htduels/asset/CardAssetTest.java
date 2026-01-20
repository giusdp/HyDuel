package com.giusdp.htduels.asset;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardAssetTest {
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
