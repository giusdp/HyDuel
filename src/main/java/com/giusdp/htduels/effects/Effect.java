package com.giusdp.htduels.effects;

import com.giusdp.htduels.duel.Duel;

@FunctionalInterface
public interface Effect {
    void apply(Duel duel);
}
