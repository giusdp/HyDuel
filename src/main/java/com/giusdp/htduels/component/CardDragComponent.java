package com.giusdp.htduels.component;

import com.giusdp.htduels.DuelsPlugin;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.Nullable;

public class CardDragComponent implements Component<EntityStore> {
    private boolean dragged;
    private @Nullable PlayerRef dragger;

    public CardDragComponent() {
    }

    public boolean isDragged() {
        return dragged;
    }

    public void setDragged(boolean dragged) {
        this.dragged = dragged;
    }

    public @Nullable PlayerRef getDragger() {
        return dragger;
    }

    public void setDragger(@Nullable PlayerRef dragger) {
        this.dragger = dragger;
    }

    public static ComponentType<EntityStore, CardDragComponent> getComponentType() {
        return DuelsPlugin.cardDragComponent;
    }

    @Override
    public @Nullable Component<EntityStore> clone() {
        return this;
    }
}
