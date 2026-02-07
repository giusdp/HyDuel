package com.giusdp.htduels.hytale.ecs.system;
import com.giusdp.htduels.hytale.DomainEventSync;

import com.giusdp.htduels.hytale.DuelManager;
import com.giusdp.htduels.match.DuelRegistry;
import com.giusdp.htduels.hytale.ecs.component.BoardLayoutComponent;
import com.giusdp.htduels.hytale.ecs.component.DuelComponent;
import com.giusdp.htduels.match.Duel;
import com.giusdp.htduels.match.event.DuelEvent;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class DuelTicker extends EntityTickingSystem<EntityStore> {

    private final DuelManager duelManager;
    private final DuelRegistry registry;
    private final DomainEventSync domainEventSync;

    public DuelTicker(DuelManager duelManager, DuelRegistry registry, DomainEventSync domainEventSync) {
        this.duelManager = duelManager;
        this.registry = registry;
        this.domainEventSync = domainEventSync;
    }

    @Override
    public void tick(float deltaTime, int index, @NonNull ArchetypeChunk<EntityStore> archetypeChunk,
                     @NonNull Store<EntityStore> store, @NonNull CommandBuffer<EntityStore> commandBuffer) {
        DuelComponent duelComp = archetypeChunk.getComponent(index, DuelComponent.getComponentType());
        BoardLayoutComponent layoutComp = archetypeChunk.getComponent(index, BoardLayoutComponent.getComponentType());
        if (duelComp == null) {
            return;
        }

        Duel duel = registry.findDuel(duelComp.getDuelId());
        if (duel == null) {
            return;
        }

        if (!duel.isSetUp()) {
            duel.setup();
            duel.flushEvents();
        } else {
            duel.tick();
            List<DuelEvent> events = duel.flushEvents();
            if (layoutComp != null && layoutComp.getBoardLayout() != null) {
                Ref<EntityStore> duelRef = archetypeChunk.getReferenceTo(index);
                domainEventSync.process(events, duelComp.getDuelId(), duelComp,
                        layoutComp.getBoardLayout(), duelRef, commandBuffer);
            }
        }

        if (duel.isFinished()) {
            Ref<EntityStore> duelRef = archetypeChunk.getReferenceTo(index);
            duelManager.cleanup(duelRef, duelComp, duel, commandBuffer);
        }
    }

    @Override
    public @Nullable Query<EntityStore> getQuery() {
        return Query.and(DuelComponent.getComponentType(), BoardLayoutComponent.getComponentType());
    }
}
