package com.giusdp.htduels.duel.zone;

import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.duel.Card;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ZoneTest {

    static Stream<Arguments> zoneProvider() {
        return Stream.of(
                Arguments.of(new Hand(), ZoneType.HAND),
                Arguments.of(new Battlefield(), ZoneType.BATTLEFIELD)
        );
    }

    private Card createCard(String id) {
        return new Card(new CardAsset(id, "Test Card", 1, 1, 1, "minion"));
    }

    @ParameterizedTest
    @MethodSource("zoneProvider")
    @DisplayName("getType returns correct zone type")
    void getTypeReturnsCorrectZoneType(Zone zone, ZoneType expectedType) {
        assertEquals(expectedType, zone.getType());
    }

    @ParameterizedTest
    @MethodSource("zoneProvider")
    @DisplayName("new zone has empty card list")
    void newZoneHasEmptyCardList(Zone zone, ZoneType expectedType) {
        assertTrue(zone.getCards().isEmpty());
    }

    @ParameterizedTest
    @MethodSource("zoneProvider")
    @DisplayName("add card makes it appear in getCards")
    void addCardAppearsInGetCards(Zone zone, ZoneType expectedType) {
        Card card = createCard("card1");

        zone.add(card, 0);

        assertEquals(1, zone.getCards().size());
        assertTrue(zone.getCards().contains(card));
    }

    @ParameterizedTest
    @MethodSource("zoneProvider")
    @DisplayName("add card at index 0 inserts at beginning")
    void addCardAtIndexZeroInsertsAtBeginning(Zone zone, ZoneType expectedType) {
        Card card1 = createCard("card1");
        Card card2 = createCard("card2");

        zone.add(card1, 0);
        zone.add(card2, 0);

        assertEquals(card2, zone.getCards().get(0));
        assertEquals(card1, zone.getCards().get(1));
    }

    @ParameterizedTest
    @MethodSource("zoneProvider")
    @DisplayName("add card at end appends to list")
    void addCardAtEndAppendsToList(Zone zone, ZoneType expectedType) {
        Card card1 = createCard("card1");
        Card card2 = createCard("card2");

        zone.add(card1, 0);
        zone.add(card2, zone.getCards().size());

        assertEquals(card1, zone.getCards().get(0));
        assertEquals(card2, zone.getCards().get(1));
    }

    @ParameterizedTest
    @MethodSource("zoneProvider")
    @DisplayName("remove card removes it from list")
    void removeCardRemovesFromList(Zone zone, ZoneType expectedType) {
        Card card = createCard("card1");
        zone.add(card, 0);

        zone.remove(card);

        assertFalse(zone.getCards().contains(card));
        assertTrue(zone.getCards().isEmpty());
    }

    @ParameterizedTest
    @MethodSource("zoneProvider")
    @DisplayName("remove card keeps other cards intact")
    void removeCardKeepsOtherCards(Zone zone, ZoneType expectedType) {
        Card card1 = createCard("card1");
        Card card2 = createCard("card2");
        Card card3 = createCard("card3");
        zone.add(card1, 0);
        zone.add(card2, 1);
        zone.add(card3, 2);

        zone.remove(card2);

        assertEquals(2, zone.getCards().size());
        assertEquals(card1, zone.getCards().get(0));
        assertEquals(card3, zone.getCards().get(1));
    }

    @ParameterizedTest
    @MethodSource("zoneProvider")
    @DisplayName("add sets card's zone reference")
    void addSetsCardZoneReference(Zone zone, ZoneType expectedType) {
        Card card = createCard("card1");

        zone.add(card, 0);

        assertSame(zone, card.getZone());
    }

    @ParameterizedTest
    @MethodSource("zoneProvider")
    @DisplayName("remove clears card's zone reference")
    void removeClearsCardZoneReference(Zone zone, ZoneType expectedType) {
        Card card = createCard("card1");
        zone.add(card, 0);

        zone.remove(card);

        assertNull(card.getZone());
    }

    @ParameterizedTest
    @MethodSource("zoneProvider")
    @DisplayName("card's getZoneIndex returns correct index")
    void cardGetZoneIndexReturnsCorrectIndex(Zone zone, ZoneType expectedType) {
        Card card1 = createCard("card1");
        Card card2 = createCard("card2");
        Card card3 = createCard("card3");

        zone.add(card1, 0);
        zone.add(card2, 1);
        zone.add(card3, 2);

        assertEquals(0, card1.getZoneIndex());
        assertEquals(1, card2.getZoneIndex());
        assertEquals(2, card3.getZoneIndex());
    }

    @ParameterizedTest
    @MethodSource("zoneProvider")
    @DisplayName("card's getZoneIndex returns -1 when not in zone")
    void cardGetZoneIndexReturnsMinusOneWhenNotInZone(Zone zone, ZoneType expectedType) {
        Card card = createCard("card1");

        assertEquals(-1, card.getZoneIndex());
    }

    @Test
    @DisplayName("add to new zone auto-removes from previous zone")
    void addToNewZoneAutoRemovesFromPreviousZone() {
        Hand hand = new Hand();
        Battlefield battlefield = new Battlefield();
        Card card = createCard("card1");

        hand.add(card, 0);
        battlefield.add(card, 0);

        assertFalse(hand.getCards().contains(card));
        assertTrue(battlefield.getCards().contains(card));
        assertSame(battlefield, card.getZone());
    }

    @Test
    @DisplayName("add to new zone updates card's zone index correctly")
    void addToNewZoneUpdatesZoneIndex() {
        Hand hand = new Hand();
        Battlefield battlefield = new Battlefield();
        Card card1 = createCard("card1");
        Card card2 = createCard("card2");

        hand.add(card1, 0);
        hand.add(card2, 1);
        assertEquals(1, card2.getZoneIndex());

        battlefield.add(card2, 0);
        assertEquals(0, card2.getZoneIndex());
        assertEquals(0, card1.getZoneIndex()); // card1 is now at index 0 in hand
    }
}