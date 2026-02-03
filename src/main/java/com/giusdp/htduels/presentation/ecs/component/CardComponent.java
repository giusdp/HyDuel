package com.giusdp.htduels.presentation.ecs.component;

import com.giusdp.htduels.DuelsPlugin;
import com.giusdp.htduels.match.CardId;
import com.giusdp.htduels.match.zone.ZoneType;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.Nullable;

public class CardComponent implements Component<EntityStore> {
    private CardId cardId;
    private Ref<EntityStore> duelEntity;
    private ZoneType zoneType;
    private int zoneIndex;
    private int zoneSize;
    private boolean opponentSide;

    public CardComponent() {
    }

    public CardComponent(CardId cardId, Ref<EntityStore> duelEntity, ZoneType zoneType, int zoneIndex, int zoneSize, boolean opponentSide) {
        this.cardId = cardId;
        this.duelEntity = duelEntity;
        this.zoneType = zoneType;
        this.zoneIndex = zoneIndex;
        this.zoneSize = zoneSize;
        this.opponentSide = opponentSide;
    }

    public CardId getCardId() {
        return cardId;
    }

    public Ref<EntityStore> getDuelEntity() {
        return duelEntity;
    }

    public ZoneType getZoneType() {
        return zoneType;
    }

    public void setZoneType(ZoneType zoneType) {
        this.zoneType = zoneType;
    }

    public int getZoneIndex() {
        return zoneIndex;
    }

    public void setZoneIndex(int zoneIndex) {
        this.zoneIndex = zoneIndex;
    }

    public int getZoneSize() {
        return zoneSize;
    }

    public void setZoneSize(int zoneSize) {
        this.zoneSize = zoneSize;
    }

    public boolean isOpponentSide() {
        return opponentSide;
    }

    public void setOpponentSide(boolean opponentSide) {
        this.opponentSide = opponentSide;
    }

    public static ComponentType<EntityStore, CardComponent> getComponentType() {
        return DuelsPlugin.cardComponent;
    }

    @Override
    public @Nullable Component<EntityStore> clone() {
        return this;
    }
}
