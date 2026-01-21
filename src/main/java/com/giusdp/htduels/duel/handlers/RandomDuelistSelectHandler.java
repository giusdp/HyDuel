package com.giusdp.htduels.duel.handlers;

import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.event.DuelEvent;
import java.util.Random;

public class RandomDuelistSelectHandler extends DuelEventHandler {
  public RandomDuelistSelectHandler(Duel duel) {
    super(duel);
  }

  @Override
  public void accept(DuelEvent ev) {
    if (new Random().nextBoolean()) {
      duel.setActiveDuelist(duel.duelist1);
    } else {
      duel.setActiveDuelist(duel.duelist2);
    }
  }
}
