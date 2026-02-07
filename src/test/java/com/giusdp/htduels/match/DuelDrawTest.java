package com.giusdp.htduels.match;

import com.giusdp.htduels.FakeCardRepo;
import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.match.event.CardsDrawn;
import com.giusdp.htduels.match.event.DuelEnded;
import com.giusdp.htduels.match.event.DuelEvent;
import com.giusdp.htduels.match.phases.DuelEndPhase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DuelDrawTest {

    private Duel duel;
    private Duelist duelist0;
    private Duelist duelist1;

    @BeforeEach
    void setUp() {
        duelist0 = new Duelist(new HumanTurnStrategy());
        duelist1 = new Duelist(new BotTurnStrategy());
        duel = Duel.builder()
                .cardRepo(new FakeCardRepo())
                .addDuelist(duelist0, false)
                .addDuelist(duelist1, true)
                .build();
        duel.setup();
    }

    private Card createCard(String id) {
        return new Card(new CardAsset(id, "Test Card", 1, 1, 1, "Minion"));
    }

    @Test
    @DisplayName("drawCards moves card from deck to hand")
    void drawCardsMovesCardFromDeckToHand() {
        Card card = createCard("card1");
        duelist0.initializeDeck(List.of(card));

        duel.drawCards(duelist0, 1);

        assertTrue(duelist0.getDeck().isEmpty());
        assertEquals(1, duelist0.getHand().getCards().size());
        assertSame(card, duelist0.getHand().getCards().getFirst());
    }

    @Test
    @DisplayName("drawn cards are tracked in cardIndex")
    void drawnCardsAreTrackedInCardIndex() {
        Card card = createCard("card1");
        duelist0.initializeDeck(List.of(card));

        duel.drawCards(duelist0, 1);

        assertSame(card, duel.findCard(card.getId()));
    }

    @Test
    @DisplayName("drawCards records CardsDrawn event")
    void drawCardsRecordsEvent() {
        Card card = createCard("card1");
        duelist0.initializeDeck(List.of(card));
        duel.flushEvents(); // Clear any setup events

        duel.drawCards(duelist0, 1);

        List<DuelEvent> events = duel.flushEvents();
        assertEquals(1, events.size());
        assertInstanceOf(CardsDrawn.class, events.getFirst());
        CardsDrawn cardsDrawn = (CardsDrawn) events.getFirst();
        assertEquals(1, cardsDrawn.cardIds.size());
        assertEquals(card.getId(), cardsDrawn.cardIds.getFirst());
    }

    @Test
    @DisplayName("multiple draws deplete deck correctly")
    void multipleDrawsDepleteDeck() {
        Card card1 = createCard("card1");
        Card card2 = createCard("card2");
        Card card3 = createCard("card3");
        duelist0.initializeDeck(List.of(card1, card2, card3));

        duel.drawCards(duelist0, 2);

        assertEquals(1, duelist0.getDeck().size());
        assertEquals(2, duelist0.getHand().getCards().size());
    }

    @Test
    @DisplayName("drawing from empty deck triggers loss")
    void emptyDeckTriggersLoss() {
        duel.drawCards(duelist0, 1);

        assertTrue(duel.isFinished());
        assertTrue(duel.isInPhase(DuelEndPhase.class));
        assertEquals(DuelEndPhase.Reason.DECK_OUT, duel.getEndReason());
    }

    @Test
    @DisplayName("partial draw when deck has fewer cards than requested")
    void partialDrawWhenDeckHasFewerCards() {
        Card card1 = createCard("card1");
        Card card2 = createCard("card2");
        duelist0.initializeDeck(List.of(card1, card2));

        duel.drawCards(duelist0, 5);

        assertEquals(2, duelist0.getHand().getCards().size());
        assertTrue(duelist0.getDeck().isEmpty());
        assertTrue(duel.isFinished());
        assertEquals(DuelEndPhase.Reason.DECK_OUT, duel.getEndReason());
    }

    @Test
    @DisplayName("partial draw records CardsDrawn event before loss")
    void partialDrawRecordsEventBeforeLoss() {
        Card card = createCard("card1");
        duelist0.initializeDeck(List.of(card));

        duel.drawCards(duelist0, 3);

        List<DuelEvent> events = duel.getAccumulatedEvents();
        boolean hasCardsDrawn = events.stream().anyMatch(e -> e instanceof CardsDrawn);
        assertTrue(hasCardsDrawn);
    }

    @Test
    @DisplayName("declareLoss transitions to DuelEndPhase with DECK_OUT reason")
    void declareLossTransitionsToEndPhase() {
        duel.declareLoss(duelist0);

        assertTrue(duel.isFinished());
        assertEquals(DuelEndPhase.Reason.DECK_OUT, duel.getEndReason());
    }

    @Test
    @DisplayName("declareLoss records DuelEnded event with correct winner/loser")
    void declareLossRecordsDuelEndedEvent() {
        duel.flushEvents(); // Clear setup events

        duel.declareLoss(duelist0);

        List<DuelEvent> events = duel.getAccumulatedEvents();
        boolean hasDuelEnded = events.stream().anyMatch(e -> e instanceof DuelEnded);
        assertTrue(hasDuelEnded);

        DuelEnded duelEnded = (DuelEnded) events.stream()
                .filter(e -> e instanceof DuelEnded)
                .findFirst()
                .orElseThrow();
        assertEquals(0, duelEnded.loserIndex);
        assertEquals(1, duelEnded.winnerIndex);
        assertEquals(DuelEndPhase.Reason.DECK_OUT, duelEnded.reason);
    }

    @Test
    @DisplayName("deck exhaustion records DuelEnded event")
    void deckExhaustionRecordsDuelEndedEvent() {
        duel.flushEvents();

        duel.drawCards(duelist0, 1); // Empty deck triggers loss

        List<DuelEvent> events = duel.getAccumulatedEvents();
        boolean hasDuelEnded = events.stream().anyMatch(e -> e instanceof DuelEnded);
        assertTrue(hasDuelEnded);
    }
}
