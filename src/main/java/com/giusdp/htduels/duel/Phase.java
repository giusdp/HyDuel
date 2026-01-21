package com.giusdp.htduels.duel;

public abstract class Phase {
  public abstract void onEnter(Duel duel);

  public abstract void tick(Duel duel);

  public abstract void onExit(Duel duel);
}
