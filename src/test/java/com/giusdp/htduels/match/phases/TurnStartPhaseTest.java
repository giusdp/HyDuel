package com.giusdp.htduels.match.phases;

import com.giusdp.htduels.FakeCardRepo;
import com.giusdp.htduels.catalog.CardAsset;
import com.giusdp.htduels.match.Card;
import com.giusdp.htduels.match.Duel;
import com.giusdp.htduels.match.event.CardsDrawn;
import com.giusdp.htduels.match.event.DuelEvent;
import com.giusdp.htduels.match.HumanTurnStrategy;
import com.giusdp.htduels.match.Duelist;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TurnStartPhaseTest {

    private List<Card> createTestDeck() {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            cards.add(new Card(new CardAsset("card" + i, "Test Card " + i, 1, 1, 1, "Minion")));
        }
        return cards;
    }

    @Test
    void turnIndicatorTextReturnsYourTurnForActiveDuelist() {
        Duelist active = new Duelist(new HumanTurnStrategy());
        assertEquals("Your Turn", TurnStartPhase.turnIndicatorText(active, active));
    }

    @Test
    void turnIndicatorTextReturnsOpponentsTurnForInactiveDuelist() {
        Duelist player = new Duelist(new HumanTurnStrategy());
        Duelist opponent = new Duelist(new HumanTurnStrategy());
        assertEquals("Opponent's Turn", TurnStartPhase.turnIndicatorText(player, opponent));
    }

    @Test
    void recordsDrawCardsEvents() {
        Duelist duelist0 = new Duelist(new HumanTurnStrategy());
        Duelist duelist1 = new Duelist(new HumanTurnStrategy());
        duelist0.initializeDeck(createTestDeck());
        duelist1.initializeDeck(createTestDeck());
        var duel = Duel.builder()
                .cardRepo(new FakeCardRepo())
                .addDuelist(duelist0, true)
                .addDuelist(duelist1, false)
                .build();
        duel.setup();
        // Tick through 10 draws in StartupPhase
        for (int i = 0; i < 10; i++) {
            duel.tick();
        }
        // Tick 11 transitions to TurnStartPhase, which draws 1 more
        duel.tick();
        List<DuelEvent> events = duel.getAccumulatedEvents();
        long drawCardsCount = events.stream().filter(e -> e instanceof CardsDrawn).count();
        assertEquals(11, drawCardsCount);
    }

}
