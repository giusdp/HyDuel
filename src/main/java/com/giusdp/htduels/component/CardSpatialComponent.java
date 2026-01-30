package com.giusdp.htduels.component;

import com.giusdp.htduels.DuelsPlugin;
import com.giusdp.htduels.duel.Card;
import com.giusdp.htduels.duel.zone.Zone;
import com.giusdp.htduels.duel.zone.ZoneType;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.math.Vec2f;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.Nullable;

public class CardSpatialComponent implements Component<EntityStore> {
    private Vec2f targetPosition;
    private float targetY;
    private float targetPitchX = Float.NaN;
    private @Nullable ZoneType lastResolvedZoneType;
    private int lastResolvedIndex = -1;

    public static ComponentType<EntityStore, CardSpatialComponent> getComponentType() {
        return DuelsPlugin.cardSpatialComponent;
    }

    public boolean needsResolution(Card card) {
        Zone zone = card.getZone();
        if (zone == null) return false;
        return lastResolvedZoneType == null
                || zone.getType() != lastResolvedZoneType
                || card.getZoneIndex() != lastResolvedIndex;
    }

    public void markResolved(Card card) {
        Zone zone = card.getZone();
        if (zone != null) {
            lastResolvedZoneType = zone.getType();
            lastResolvedIndex = card.getZoneIndex();
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

    public float getTargetPitchX() {
        return targetPitchX;
    }

    public void setTargetPitchX(float targetPitchX) {
        this.targetPitchX = targetPitchX;
    }

    @Override
    public @Nullable Component<EntityStore> clone() {
        return this;
    }
}
