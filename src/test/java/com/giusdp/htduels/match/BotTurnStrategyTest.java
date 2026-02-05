package com.giusdp.htduels.match;

import com.giusdp.htduels.FakeCardRepo;
import com.giusdp.htduels.catalog.CardAsset;
import com.giusdp.htduels.match.event.DuelEvent;
import com.giusdp.htduels.match.event.MainPhaseEnded;
import com.giusdp.htduels.match.event.CardPlayed;
import com.giusdp.htduels.match.phases.MainPhase;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BotTurnStrategyTest {

    private List<Card> createTestDeck() {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            cards.add(new Card(new CardAsset("card" + i, "Test Card " + i, 1, 1, 1, "Minion")));
        }
        return cards;
    }

    private Duel createDuelInMainPhase(Duelist bot) {
        Duelist player = new Duelist(new HumanTurnStrategy());
        player.initializeDeck(createTestDeck());
        bot.initializeDeck(createTestDeck());
        Duel duel = Duel.builder()
                .cardRepo(new FakeCardRepo())
                .addDuelist(player, true)
                .addDuelist(bot, false)
                .build();
        duel.setup();
        // Force active duelist to player so Bot doesn't auto-trigger during ticks
        duel.setActiveDuelist(player);
        // Tick through StartupPhase (10 draws) + TurnStartPhase + into MainPhase
        for (int i = 0; i < 12; i++) {
            duel.tick();
        }
        assertTrue(duel.isInPhase(MainPhase.class));
        duel.flushEvents();
        return duel;
    }

    @Test
    void takeTurnPlaysCardAndEndsPhase() {
        Duelist bot = new Duelist(new BotTurnStrategy());
        Duel duel = createDuelInMainPhase(bot);

        Card card = new Card(new CardAsset("test", "Test Card", 1, 2, 3, "Minion"));
        bot.addToHand(card);

        bot.takeTurn(duel);

        List<DuelEvent> events = duel.getAccumulatedEvents();
        boolean hasPlayCard = events.stream().anyMatch(e -> e instanceof CardPlayed pc && pc.cardId.equals(card.getId()));
        boolean hasEndMainPhase = events.stream().anyMatch(e -> e instanceof MainPhaseEnded);

        assertTrue(hasPlayCard);
        assertTrue(hasEndMainPhase);
    }

    @Test
    void takeTurnEndsMainPhaseWhenHandIsEmpty() {
        Duelist bot = new Duelist(new BotTurnStrategy());
        Duel duel = createDuelInMainPhase(bot);
        // Clear bot's hand from startup draws
        bot.getHand().getCards().clear();

        bot.takeTurn(duel);

        var events = duel.getAccumulatedEvents();
        assertEquals(1, events.size());
        assertInstanceOf(MainPhaseEnded.class, events.getFirst());
    }
}
