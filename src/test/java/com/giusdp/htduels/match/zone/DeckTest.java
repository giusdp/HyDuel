package com.giusdp.htduels.match.zone;

import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.match.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    private Deck deck;

    @BeforeEach
    void setUp() {
        deck = new Deck();
    }

    private Card createCard(String id) {
        return new Card(new CardAsset(id, "Test Card", 1, 1, 1, "minion"));
    }

    // === drawTop tests ===

    @Test
    @DisplayName("drawTop returns null when deck is empty")
    void drawTopReturnsNullWhenEmpty() {
        assertNull(deck.drawTop());
    }

    @Test
    @DisplayName("drawTop returns the last card added (top of deck)")
    void drawTopReturnsLastCardAdded() {
        Card card1 = createCard("card1");
        Card card2 = createCard("card2");
        Card card3 = createCard("card3");

        deck.place(card1, 0);
        deck.place(card2, 1);
        deck.place(card3, 2);

        assertSame(card3, deck.drawTop());
    }

    @Test
    @DisplayName("drawTop removes the card from deck")
    void drawTopRemovesCardFromDeck() {
        Card card = createCard("card1");
        deck.place(card, 0);

        deck.drawTop();

        assertTrue(deck.isEmpty());
        assertFalse(deck.getCards().contains(card));
    }

    @Test
    @DisplayName("drawTop clears card's zone reference")
    void drawTopClearsZoneReference() {
        Card card = createCard("card1");
        deck.place(card, 0);

        Card drawn = deck.drawTop();

        assertNull(drawn.getZone());
    }

    @Test
    @DisplayName("drawTop returns cards in LIFO order")
    void drawTopReturnsCardsInLifoOrder() {
        Card card1 = createCard("card1");
        Card card2 = createCard("card2");
        Card card3 = createCard("card3");

        deck.place(card1, 0);
        deck.place(card2, 1);
        deck.place(card3, 2);

        assertSame(card3, deck.drawTop());
        assertSame(card2, deck.drawTop());
        assertSame(card1, deck.drawTop());
        assertNull(deck.drawTop());
    }

    @Test
    @DisplayName("multiple drawTop calls deplete deck correctly")
    void multipleDrawTopCallsDepleteDeck() {
        for (int i = 0; i < 5; i++) {
            deck.place(createCard("card" + i), i);
        }

        assertEquals(5, deck.size());

        for (int i = 0; i < 5; i++) {
            assertNotNull(deck.drawTop());
        }

        assertEquals(0, deck.size());
        assertTrue(deck.isEmpty());
    }

    // === isEmpty tests ===

    @Test
    @DisplayName("isEmpty returns true for new deck")
    void isEmptyReturnsTrueForNewDeck() {
        assertTrue(deck.isEmpty());
    }

    @Test
    @DisplayName("isEmpty returns false after adding card")
    void isEmptyReturnsFalseAfterAddingCard() {
        deck.place(createCard("card1"), 0);
        assertFalse(deck.isEmpty());
    }

    @Test
    @DisplayName("isEmpty returns true after all cards drawn")
    void isEmptyReturnsTrueAfterAllCardsDrawn() {
        deck.place(createCard("card1"), 0);
        deck.drawTop();
        assertTrue(deck.isEmpty());
    }

    // === size tests ===

    @Test
    @DisplayName("size returns 0 for new deck")
    void sizeReturnsZeroForNewDeck() {
        assertEquals(0, deck.size());
    }

    @Test
    @DisplayName("size returns correct count after adding cards")
    void sizeReturnsCorrectCountAfterAdding() {
        deck.place(createCard("card1"), 0);
        deck.place(createCard("card2"), 1);
        deck.place(createCard("card3"), 2);

        assertEquals(3, deck.size());
    }

    @Test
    @DisplayName("size decreases after drawTop")
    void sizeDecreasesAfterDrawTop() {
        deck.place(createCard("card1"), 0);
        deck.place(createCard("card2"), 1);

        assertEquals(2, deck.size());
        deck.drawTop();
        assertEquals(1, deck.size());
    }

    // === shuffle tests ===

    @RepeatedTest(5)
    @DisplayName("shuffle changes card order (probabilistic)")
    void shuffleChangesCardOrder() {
        List<Card> originalOrder = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Card card = createCard("card" + i);
            originalOrder.add(card);
            deck.place(card, i);
        }

        deck.shuffle();

        // With 20 cards, probability of same order is 1/20! ≈ 0
        // Check if at least one card changed position
        boolean orderChanged = false;
        for (int i = 0; i < 20; i++) {
            if (deck.getCards().get(i) != originalOrder.get(i)) {
                orderChanged = true;
                break;
            }
        }
        assertTrue(orderChanged, "Shuffle should change card order");
    }

    @Test
    @DisplayName("shuffle preserves all cards")
    void shufflePreservesAllCards() {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Card card = createCard("card" + i);
            cards.add(card);
            deck.place(card, i);
        }

        deck.shuffle();

        assertEquals(10, deck.size());
        for (Card card : cards) {
            assertTrue(deck.getCards().contains(card));
        }
    }

    @Test
    @DisplayName("shuffle on empty deck does nothing")
    void shuffleOnEmptyDeckDoesNothing() {
        assertDoesNotThrow(() -> deck.shuffle());
        assertTrue(deck.isEmpty());
    }

    @Test
    @DisplayName("shuffle on single card deck preserves card")
    void shuffleOnSingleCardDeckPreservesCard() {
        Card card = createCard("card1");
        deck.place(card, 0);

        deck.shuffle();

        assertEquals(1, deck.size());
        assertSame(card, deck.getCards().getFirst());
    }
}
