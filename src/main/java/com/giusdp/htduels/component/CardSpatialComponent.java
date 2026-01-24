package com.giusdp.htduels.component;

import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.math.Vec2f;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.Nullable;

public class CardSpatialComponent implements Component<EntityStore> {
    private Vec2f targetPosition;
    private boolean dirty = true;

    public boolean isDirty() {
        return dirty;
    }

    public void markDirty() {
        dirty = true;
    }

    public void clearDirty() {
        dirty = false;
    }

    public Vec2f getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(Vec2f pos) {
        this.targetPosition = pos;
    }

    @Override
    public @Nullable Component<EntityStore> clone() {
        return this;
    }
}
