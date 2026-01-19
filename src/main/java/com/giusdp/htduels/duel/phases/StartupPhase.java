package com.giusdp.htduels.duel.phases;

import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.Phase;
import com.giusdp.htduels.effects.EffectRepo;

import java.util.List;
import java.util.function.Function;

public class StartupPhase extends Phase {
    public StartupPhase() {
        registerEffect(EffectRepo.fillHands());
    }
    @Override
    public void onEnter(Duel duel) {
        applyEffects(duel);
    }

    @Override
    public void tick(Duel duel) {
        duel.transitionTo(new TurnStartPhase());
    }

    @Override
    public void onExit(Duel duel) {
    }
}
