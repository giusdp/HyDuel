package com.giusdp.htduels.component;

import com.giusdp.htduels.DuelsPlugin;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class CardComponent implements Component<EntityStore> {
    public String name = "Im a proto card";


    public CardComponent() {
    }

    public static ComponentType<EntityStore, CardComponent> getComponentType() {
        return DuelsPlugin.cardComponent;
    }

    @Override
    public Component<EntityStore> clone() {
        CardComponent component = new CardComponent();
        component.name = this.name;
        return component;
    }
}
