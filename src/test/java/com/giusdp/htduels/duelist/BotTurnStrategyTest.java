package com.giusdp.htduels.duelist;

import com.giusdp.htduels.FakeCardRepo;
import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.duel.Card;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.event.DuelEvent;
import com.giusdp.htduels.duel.event.MainPhaseEnded;
import com.giusdp.htduels.duel.event.CardPlayed;
import com.giusdp.htduels.duel.phases.MainPhase;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BotTurnStrategyTest {

    private Duel createDuelInMainPhase(Duelist bot) {
        Duelist player = new Duelist(new PlayerTurnStrategy());
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
        boolean hasPlayCard = events.stream().anyMatch(e -> e instanceof CardPlayed pc && pc.card.equals(card));
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
