package com.giusdp.htduels.duel;

import com.giusdp.htduels.effects.Effect;

import java.util.ArrayList;
import java.util.List;

public abstract class Phase {
    List<Effect> effects = new ArrayList<>();

    public abstract void onEnter(Duel duel);

    public abstract void tick(Duel duel);

    public abstract void onExit(Duel duel);

    protected void applyEffects(Duel duel) {
        effects.forEach(effect -> effect.apply(duel));
    }

    protected void registerEffect(Effect effect) {
        effects.add(effect);
    }
}
