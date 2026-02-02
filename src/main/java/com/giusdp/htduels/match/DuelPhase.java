package com.giusdp.htduels.match;

public abstract class DuelPhase {
    public abstract void onEnter(Duel duel);

    public abstract void tick(Duel duel);

    public abstract void onExit(Duel duel);
}
