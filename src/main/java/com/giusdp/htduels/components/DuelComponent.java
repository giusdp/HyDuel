package com.giusdp.htduels.components;

import com.giusdp.htduels.DuelsPlugin;
import com.giusdp.htduels.duel.Duel;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class DuelComponent implements Component<EntityStore> {
    public Duel duel;

    public DuelComponent() {
        duel = new Duel();
    }

    public static ComponentType<EntityStore, DuelComponent> getComponentType() {
        return DuelsPlugin.duelComponent;
    }

    @Override
    public Component<EntityStore> clone() {
        DuelComponent component = new DuelComponent();
        return component;
    }
}
