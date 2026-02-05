package com.giusdp.htduels.hytale.ecs.component;

import com.giusdp.htduels.DuelsPlugin;
import com.giusdp.htduels.match.DuelId;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DuelComponent implements Component<EntityStore> {
    private DuelId duelId;
    private final List<Ref<EntityStore>> cardEntities = new ArrayList<>();

    public DuelComponent() {
        this.duelId = null;
    }

    public DuelComponent(DuelId duelId) {
        this.duelId = duelId;
    }

    public DuelId getDuelId() {
        return duelId;
    }

    public List<Ref<EntityStore>> getCardEntities() {
        return Collections.unmodifiableList(cardEntities);
    }

    public void addCardEntity(Ref<EntityStore> cardRef) {
        cardEntities.add(cardRef);
    }

    public static ComponentType<EntityStore, DuelComponent> getComponentType() {
        return DuelsPlugin.duelComponent;
    }

    @Override
    public Component<EntityStore> clone() {
        return this;
    }
}
