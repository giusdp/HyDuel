package com.giusdp.htduels.system;

import com.giusdp.htduels.component.DuelComponent;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class DuelTicker extends EntityTickingSystem<EntityStore> {

    @Override
    public void tick(float deltaTime, int index, @NonNull ArchetypeChunk<EntityStore> archetypeChunk,
                     @NonNull Store<EntityStore> store, @NonNull CommandBuffer<EntityStore> commandBuffer) {
        DuelComponent duelComponent = archetypeChunk.getComponent(index, DuelComponent.getComponentType());
        if (duelComponent == null) {
            return;
        }
        duelComponent.duel.tick();
    }

    @Override
    public @Nullable Query<EntityStore> getQuery() {
        return Query.and(DuelComponent.getComponentType());
    }
}
