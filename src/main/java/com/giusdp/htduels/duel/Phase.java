package com.giusdp.htduels.duel;

public interface Phase {
    void onEnter(Duel duel);
    void tick(Duel duel);
    void onExit(Duel duel);
}
