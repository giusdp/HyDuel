package com.giusdp.htduels.component;

import com.giusdp.htduels.DuelsPlugin;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.Nullable;

public class CardHoverComponent implements Component<EntityStore> {
    private float originalY;
    private boolean hovered;

    public CardHoverComponent() {
    }

    public CardHoverComponent(float originalY) {
        this.originalY = originalY;
        this.hovered = false;
    }

    public float getOriginalY() {
        return originalY;
    }

    public boolean isHovered() {
        return hovered;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    public static ComponentType<EntityStore, CardHoverComponent> getComponentType() {
        return DuelsPlugin.cardHoverComponent;
    }

    @Override
    public @Nullable Component<EntityStore> clone() {
        return this;
    }
}
