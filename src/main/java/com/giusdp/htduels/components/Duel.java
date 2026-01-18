package com.giusdp.htduels.components;

import com.giusdp.htduels.DuelsPlugin;
import com.giusdp.htduels.duel.Phase;
import com.giusdp.htduels.duel.phases.StartupPhase;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.Nullable;

public class Duel implements Component<EntityStore> {

    private Phase currentPhase = new StartupPhase();

    public static ComponentType<EntityStore, Duel> getComponentType() {
        return DuelsPlugin.duelComponent;
    }

    public void tick() {
        currentPhase.tick(this);
    }

    public void transitionTo(Phase newPhase) {
        currentPhase.onExit(this);
        currentPhase = newPhase;
        currentPhase.onEnter(this);
    }

    public Phase getCurrentPhase() {
        return currentPhase;
    }

    @Override
    public @Nullable Component<EntityStore> clone() {
        return new Duel();
    }
}
