package com.giusdp.htduels.system;

import com.giusdp.htduels.component.CardSpatialComponent;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class CardRotationSystem extends EntityTickingSystem<EntityStore> {
    @Override
    public void tick(float deltaTime, int index, @NonNull ArchetypeChunk<EntityStore> archetypeChunk,
                     @NonNull Store<EntityStore> store, @NonNull CommandBuffer<EntityStore> commandBuffer) {
        CardSpatialComponent spatial = archetypeChunk.getComponent(index, CardSpatialComponent.getComponentType());
        if (spatial == null) {
            return;
        }

        float targetPitch = spatial.getTargetPitchX();
        if (Float.isNaN(targetPitch)) {
            return;
        }

        TransformComponent transform = archetypeChunk.getComponent(index, TransformComponent.getComponentType());
        if (transform == null) {
            return;
        }

        Vector3f rotation = transform.getRotation();
        if (rotation.getPitch() != targetPitch) {
            transform.setRotation(new Vector3f(targetPitch, rotation.getYaw(), rotation.getRoll()));
        }
    }

    @Override
    public @Nullable Query<EntityStore> getQuery() {
        return Query.and(CardSpatialComponent.getComponentType());
    }
}
