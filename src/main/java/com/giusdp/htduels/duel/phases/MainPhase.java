package com.giusdp.htduels.duel.phases;

import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.Phase;
import com.giusdp.htduels.duelist.Bot;

public class MainPhase extends Phase {

  @Override
  public void onEnter(Duel duel) {
  }

  @Override
  public void tick(Duel duel) {
    // Waiting for player input
    if (duel.activeDuelist instanceof Bot) {
      Bot bot = (Bot) duel.activeDuelist;
      bot.playTurn(duel);
    }
  }

  @Override
  public void onExit(Duel duel) {
  }
}
