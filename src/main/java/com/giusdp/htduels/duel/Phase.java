package com.giusdp.htduels.duel;

import com.giusdp.htduels.components.Duel;

public interface Phase {
    void onEnter(Duel duel);
    void tick(Duel duel);
    void onExit(Duel duel);
}
