package com.giusdp.htduels.duel.phases;

import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.Phase;

public class TurnEndPhase extends Phase {

  @Override
  public void onEnter(Duel duel) {
  }

  @Override
  public void tick(Duel duel) {
    duel.transitionTo(new TurnStartPhase());
  }

  @Override
  public void onExit(Duel duel) {
  }
}
