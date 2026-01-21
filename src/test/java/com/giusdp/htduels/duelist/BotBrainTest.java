package com.giusdp.htduels.duelist;

import static org.junit.jupiter.api.Assertions.*;

import com.giusdp.htduels.FakeCardRepo;
import com.giusdp.htduels.FakeEventBus;
import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.event.DuelEvent;
import com.giusdp.htduels.duel.event.PlayCard;
import java.util.List;
import org.junit.jupiter.api.Test;

class BotBrainTest {

  @Test
  void takeTurnEmitsPlayCardEventWhenBotHasCards() {
    FakeEventBus eventBus = new FakeEventBus();
    Bot bot = new Bot();
    Duel duel = new Duel(new DuelPlayer(), bot, eventBus, new FakeCardRepo());

    CardAsset card = new CardAsset("test", "Test Card", 1, 2, 3, "Minion");
    bot.addToHand(card);

    BotBrain brain = new BotBrain(bot);
    brain.takeTurn(duel);

    List<DuelEvent> events = eventBus.postedEvents();
    boolean hasPlayCard = events.stream().anyMatch(e -> e instanceof PlayCard pc && pc.card().equals(card));

    assertTrue(hasPlayCard);
  }

  @Test
  void takeTurnDoesNothingWhenHandIsEmpty() {
    FakeEventBus eventBus = new FakeEventBus();
    Bot bot = new Bot();
    Duel duel = new Duel(new DuelPlayer(), bot, eventBus, new FakeCardRepo());

    int eventCountBefore = eventBus.postedEvents().size();

    BotBrain brain = new BotBrain(bot);
    brain.takeTurn(duel);

    int eventCountAfter = eventBus.postedEvents().size();

    assertEquals(eventCountBefore, eventCountAfter);
  }
}
