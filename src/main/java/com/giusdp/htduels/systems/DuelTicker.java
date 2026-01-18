package com.giusdp.htduels.systems;

import com.giusdp.htduels.components.Duel;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class DuelTicker extends EntityTickingSystem<EntityStore> {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    @Override
    public void tick(float deltaTime, int index,
                     @NonNull ArchetypeChunk<EntityStore> archetypeChunk,
                     @NonNull Store<EntityStore> store,
                     @NonNull CommandBuffer<EntityStore> commandBuffer) {
        Duel duel = archetypeChunk.getComponent(index, Duel.getComponentType());
        if (duel == null) return;
        duel.tick();
    }


    @Override
    public @Nullable Query<EntityStore> getQuery() {
        return Query.and(Duel.getComponentType());
    }
}
