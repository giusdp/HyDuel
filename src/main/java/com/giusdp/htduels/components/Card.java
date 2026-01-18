package com.giusdp.htduels.components;

import com.giusdp.htduels.DuelsPlugin;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

/**
 * Component that marks an entity as a card.
 * Stores card-specific data.
 */
public class Card implements Component<EntityStore> {
    public String name = "Im a proto card";


    public Card() {
    }

    public static ComponentType<EntityStore, Card> getComponentType() {
        return DuelsPlugin.cardComponent;
    }

    @Override
    public Component<EntityStore> clone() {
        Card component = new Card();
        component.name = this.name;
        return component;
    }
}
