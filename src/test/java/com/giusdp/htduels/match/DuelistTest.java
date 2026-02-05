package com.giusdp.htduels.match;

import com.giusdp.htduels.catalog.CardAsset;
import com.giusdp.htduels.match.zone.Deck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DuelistTest {

    private Duelist duelist;

    @BeforeEach
    void setUp() {
        duelist = new Duelist(new HumanTurnStrategy());
    }

    private Card createCard(String id) {
        return new Card(new CardAsset(id, "Test Card", 1, 1, 1, "minion"));
    }

    @Test
    @DisplayName("getDeck returns the deck zone")
    void getDeckReturnsDeckZone() {
        Deck deck = duelist.getDeck();
        assertNotNull(deck);
    }

    @Test
    @DisplayName("initializeDeck populates deck with cards")
    void initializeDeckPopulatesDeck() {
        Card card1 = createCard("card1");
        Card card2 = createCard("card2");
        Card card3 = createCard("card3");

        duelist.initializeDeck(List.of(card1, card2, card3));

        assertEquals(3, duelist.getDeck().size());
        assertTrue(duelist.getDeck().getCards().contains(card1));
        assertTrue(duelist.getDeck().getCards().contains(card2));
        assertTrue(duelist.getDeck().getCards().contains(card3));
    }

    @Test
    @DisplayName("initializeDeck sets card zones correctly")
    void initializeDeckSetsCardZones() {
        Card card = createCard("card1");

        duelist.initializeDeck(List.of(card));

        assertSame(duelist.getDeck(), card.getZone());
    }

    @Test
    @DisplayName("deck is empty by default")
    void deckIsEmptyByDefault() {
        assertTrue(duelist.getDeck().isEmpty());
    }
}
