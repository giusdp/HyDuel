package com.giusdp.htduels.hytale.ecs.component;

import com.giusdp.htduels.DuelsPlugin;
import com.giusdp.htduels.match.zone.ZoneType;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.math.Vec2f;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.Nullable;

public class CardSpatialComponent implements Component<EntityStore> {
    private Vec2f targetPosition;
    private float targetY;
    private @Nullable ZoneType lastResolvedZoneType;
    private int lastResolvedIndex = -1;
    private int lastResolvedSize = -1;

    public static ComponentType<EntityStore, CardSpatialComponent> getComponentType() {
        return DuelsPlugin.cardSpatialComponent;
    }

    public boolean needsResolution(CardComponent cc) {
        if (cc.getZoneType() == null) return false;
        return lastResolvedZoneType == null
                || cc.getZoneType() != lastResolvedZoneType
                || cc.getZoneIndex() != lastResolvedIndex
                || cc.getZoneSize() != lastResolvedSize;
    }

    public void markResolved(CardComponent cc) {
        if (cc.getZoneType() != null) {
            lastResolvedZoneType = cc.getZoneType();
            lastResolvedIndex = cc.getZoneIndex();
            lastResolvedSize = cc.getZoneSize();
        }
    }

    public Vec2f getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(Vec2f pos) {
        this.targetPosition = pos;
    }

    public float getTargetY() {
        return targetY;
    }

    public void setTargetY(float targetY) {
        this.targetY = targetY;
    }

    @Override
    public @Nullable Component<EntityStore> clone() {
        return this;
    }
}
