package com.giusdp.htduels.duelist;

import com.giusdp.htduels.FakeCardRepo;
import com.giusdp.htduels.FakeEventBus;
import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.duel.Card;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.event.DuelEvent;
import com.giusdp.htduels.duel.event.EndMainPhase;
import com.giusdp.htduels.duel.event.PlayCard;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BotTest {

    @Test
    void takeTurnEmitsPlayCardEventWhenBotHasCards() {
        FakeEventBus eventBus = new FakeEventBus();
        Bot bot = new Bot();
        Duel duel = new Duel(new DuelPlayer(), bot, eventBus, new FakeCardRepo());

        Card card = new Card(new CardAsset("test", "Test Card", 1, 2, 3, "Minion"));
        bot.addToHand(card);

        bot.playTurn(duel);

        List<DuelEvent> events = eventBus.postedEvents();
        boolean hasPlayCard = events.stream().anyMatch(e -> e instanceof PlayCard pc && pc.card.equals(card));

        assertTrue(hasPlayCard);
    }

    @Test
    void takeTurnDoesNothingWhenHandIsEmpty() {
        FakeEventBus eventBus = new FakeEventBus();
        Bot bot = new Bot();
        Duel duel = new Duel(new DuelPlayer(), bot, eventBus, new FakeCardRepo());

        bot.playTurn(duel);

        var evs = eventBus.postedEvents();
        assertEquals(1, evs.size());
        assertInstanceOf(EndMainPhase.class, evs.getFirst());
    }
}
