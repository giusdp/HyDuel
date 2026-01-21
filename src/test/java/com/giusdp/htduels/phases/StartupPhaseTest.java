package com.giusdp.htduels.phases;

import com.giusdp.htduels.FakeCardRepo;
import com.giusdp.htduels.FakeEventBus;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.event.DrawCards;
import com.giusdp.htduels.duel.event.DuelEvent;
import com.giusdp.htduels.duel.event.DuelStarted;
import com.giusdp.htduels.duel.event.RandomDuelistSelect;
import com.giusdp.htduels.duelist.DuelPlayer;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StartupPhaseTest {
  FakeEventBus eventBus ;

  @BeforeEach void setup() {
    this.eventBus = new FakeEventBus();
    var duel = new Duel(new DuelPlayer(), new DuelPlayer(), eventBus, new FakeCardRepo());
    duel.setup();
  }

  @Test
  void emitsDuelStarted() {
    List<DuelEvent> moves = eventBus.postedEvents();
    assertInstanceOf(DuelStarted.class, moves.getFirst());
  }

  @Test
  void emitsDrawCardsMoves() {
    List<DuelEvent> moves = eventBus.postedEvents();
    long drawCardsCount = moves.stream().filter(m -> m instanceof DrawCards).count();
    assertEquals(2, drawCardsCount);
  }

  @Test
  void startupPhaseEmitsRandomDuelistSelect() {
    var moves = eventBus.postedEvents().stream().filter(e -> e instanceof RandomDuelistSelect).toList();
    assertFalse(moves.isEmpty());
  }
}
