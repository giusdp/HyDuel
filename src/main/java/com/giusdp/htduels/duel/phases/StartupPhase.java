package com.giusdp.htduels.duel.phases;

import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.Phase;
import com.giusdp.htduels.duel.event.DrawCards;
import com.giusdp.htduels.duel.event.DuelStarted;
import com.giusdp.htduels.duel.event.RandomDuelistSelect;

public class StartupPhase extends Phase {

  @Override
  public void onEnter(Duel duel) {
    duel.emit(new DuelStarted());
    duel.emit(new DrawCards(duel.duelist1, 5));
    duel.emit(new DrawCards(duel.duelist2, 5));
    duel.emit(new RandomDuelistSelect());
  }

  @Override
  public void tick(Duel duel) {
    duel.transitionTo(new TurnStartPhase());
  }

  @Override
  public void onExit(Duel duel) {
  }
}
