package com.giusdp.htduels.match.deck;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeckRulesTest {

    private final DeckRules rules = new DeckRules();

    // canAddCard tests

    @Test
    @DisplayName("canAddCard returns true for empty deck")
    void canAddCardReturnsTrueForEmptyDeck() {
        assertTrue(rules.canAddCard(Collections.emptyList(), "card_a"));
    }

    @Test
    @DisplayName("canAddCard returns true when 1 copy exists")
    void canAddCardReturnsTrueWhenOneCopyExists() {
        List<String> deck = List.of("card_a");
        assertTrue(rules.canAddCard(deck, "card_a"));
    }

    @Test
    @DisplayName("canAddCard returns false when 2 copies exist")
    void canAddCardReturnsFalseWhenTwoCopiesExist() {
        List<String> deck = List.of("card_a", "card_a");
        assertFalse(rules.canAddCard(deck, "card_a"));
    }

    @Test
    @DisplayName("canAddCard returns true for different card when at limit")
    void canAddCardReturnsTrueForDifferentCardWhenAtLimit() {
        List<String> deck = List.of("card_a", "card_a");
        assertTrue(rules.canAddCard(deck, "card_b"));
    }

    @Test
    @DisplayName("canAddCard returns false in mixed deck at limit for card")
    void canAddCardReturnsFalseInMixedDeckAtLimit() {
        List<String> deck = List.of("a", "a", "b", "b");
        assertFalse(rules.canAddCard(deck, "a"));
    }

    @Test
    @DisplayName("canAddCard returns true in mixed deck not at limit for card")
    void canAddCardReturnsTrueInMixedDeckNotAtLimit() {
        List<String> deck = List.of("a", "a", "b");
        assertTrue(rules.canAddCard(deck, "b"));
    }

    @ParameterizedTest
    @CsvSource({
            "0, true",
            "1, true",
            "2, false"
    })
    @DisplayName("canAddCard enforces 2-copy limit")
    void canAddCardEnforcesTwoCopyLimit(int existingCopies, boolean expected) {
        List<String> deck = Collections.nCopies(existingCopies, "card_a");
        assertEquals(expected, rules.canAddCard(deck, "card_a"));
    }

    // isValidForDuel tests

    @ParameterizedTest
    @CsvSource({
            "0, false",
            "19, false",
            "20, true",
            "21, false"
    })
    @DisplayName("isValidForDuel requires exactly 20 cards")
    void isValidForDuelRequiresExactly20Cards(int count, boolean expected) {
        assertEquals(expected, rules.isValidForDuel(count));
    }

    // cardsNeeded tests

    @ParameterizedTest
    @CsvSource({
            "0, 20",
            "15, 5",
            "20, 0",
            "25, 0"
    })
    @DisplayName("cardsNeeded returns remaining cards to reach 20")
    void cardsNeededReturnsRemainingCards(int current, int expected) {
        assertEquals(expected, rules.cardsNeeded(current));
    }
}
