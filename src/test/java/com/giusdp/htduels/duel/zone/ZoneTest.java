package com.giusdp.htduels.duel.zone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.duel.Card;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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
}