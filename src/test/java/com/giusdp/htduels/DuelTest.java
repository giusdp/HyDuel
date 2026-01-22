package com.giusdp.htduels;

import static org.junit.jupiter.api.Assertions.*;

import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.event.DuelEvent;
import com.giusdp.htduels.duel.event.DuelStarted;
import com.giusdp.htduels.duel.phases.StartupPhase;
import com.giusdp.htduels.duel.phases.TurnStartPhase;
import com.giusdp.htduels.duelist.Bot;
import com.giusdp.htduels.duelist.DuelPlayer;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DuelTest {
  Duel duel;

  @BeforeEach void setup() {
    duel = new Duel(new DuelPlayer(), new Bot(), new FakeEventBus(), new FakeCardRepo());
    duel.setup();
  }

  @Test
  void newDuelStartsInStartupAndTransitionsToTurnStart() {
    assertEquals(StartupPhase.class, duel.currentPhase.getClass());
    duel.tick();
    assertEquals(TurnStartPhase.class, duel.currentPhase.getClass());
  }

  @Test
  void handsGetFilledOnStartup() {
    assertEquals(5, duel.duelist1.getHand().getCards().size());
    assertEquals(5, duel.duelist2.getHand().getCards().size());
  }
}
