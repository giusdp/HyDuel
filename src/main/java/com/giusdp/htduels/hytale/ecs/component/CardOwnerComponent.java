package com.giusdp.htduels.hytale.ecs.component;

import com.giusdp.htduels.DuelsPlugin;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.Nullable;

public class CardOwnerComponent implements Component<EntityStore> {
    private final @Nullable PlayerRef ownerPlayerRef;

    public CardOwnerComponent() {
        this(null);
    }

    public CardOwnerComponent(@Nullable PlayerRef ownerPlayerRef) {
        this.ownerPlayerRef = ownerPlayerRef;
    }

    public @Nullable PlayerRef getOwnerPlayerRef() {
        return ownerPlayerRef;
    }

    public static ComponentType<EntityStore, CardOwnerComponent> getComponentType() {
        return DuelsPlugin.cardOwnerComponent;
    }

    @Override
    public @Nullable Component<EntityStore> clone() {
        return this;
    }
}
