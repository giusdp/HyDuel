package com.giusdp.htduels.duel.phases;

import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.Phase;
import com.giusdp.htduels.duel.event.DrawCards;

public class TurnStartPhase extends Phase {

  @Override
  public void onEnter(Duel duel) {
    duel.emit(new DrawCards(duel.activeDuelist, 1));
  }

  @Override
  public void tick(Duel duel) {

    duel.transitionTo(new MainPhase());
  }

  @Override
  public void onExit(Duel duel) {
  }
}
