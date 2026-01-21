package com.giusdp.htduels.handlers;

import com.giusdp.htduels.FakeCardRepo;
import com.giusdp.htduels.FakeEventBus;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.event.DrawCards;
import com.giusdp.htduels.duel.event.EndMainPhase;
import com.giusdp.htduels.duel.phases.MainPhase;
import com.giusdp.htduels.duel.phases.TurnEndPhase;
import com.giusdp.htduels.duelist.Bot;
import com.giusdp.htduels.duelist.DuelPlayer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EndMainPhaseHandlerTest {
  @Test
  void addsCardsToHand() {
    Duel duel = new Duel(new DuelPlayer(), new Bot(), new FakeEventBus(), new FakeCardRepo());
    duel.setup();
    duel.tick();
    duel.tick();

    assertInstanceOf(MainPhase.class, duel.currentPhase);

    duel.emit(new EndMainPhase());
    assertInstanceOf(TurnEndPhase.class, duel.currentPhase);
  }
}
