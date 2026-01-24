package com.giusdp.htduels.system;

import com.giusdp.htduels.component.CardComponent;
import com.giusdp.htduels.duel.Card;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class CardSpatialResolutionSystem extends EntityTickingSystem<EntityStore> {
    @Override
    public void tick(float deltaTime, int index, @NonNull ArchetypeChunk<EntityStore> archetypeChunk,
                     @NonNull Store<EntityStore> store, @NonNull CommandBuffer<EntityStore> commandBuffer) {
        CardComponent cardComponent = archetypeChunk.getComponent(index, CardComponent.getComponentType());
        if (cardComponent == null) {
            return;
        }

        Card card = cardComponent.getCard();

        TransformComponent transform = archetypeChunk.getComponent(index, TransformComponent.getComponentType());
        if (transform != null) {
            //transform.setPosition();
        }
    }

    @Override
    public @Nullable Query<EntityStore> getQuery() {
        return Query.and(CardComponent.getComponentType());
    }
}
