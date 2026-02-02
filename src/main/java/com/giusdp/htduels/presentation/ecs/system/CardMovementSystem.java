package com.giusdp.htduels.presentation.ecs.system;

import com.giusdp.htduels.presentation.ecs.component.CardDragComponent;
import com.giusdp.htduels.presentation.ecs.component.CardSpatialComponent;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.math.Vec2f;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class CardMovementSystem extends EntityTickingSystem<EntityStore> {
    @Override
    public void tick(float deltaTime, int index, @NonNull ArchetypeChunk<EntityStore> archetypeChunk,
                     @NonNull Store<EntityStore> store, @NonNull CommandBuffer<EntityStore> commandBuffer) {
        CardSpatialComponent spatial = archetypeChunk.getComponent(index, CardSpatialComponent.getComponentType());
        if (spatial == null || spatial.getTargetPosition() == null) {
            return;
        }

        CardDragComponent drag = archetypeChunk.getComponent(index, CardDragComponent.getComponentType());
        if (drag != null && drag.isDragged()) {
            return;
        }

        TransformComponent transform = archetypeChunk.getComponent(index, TransformComponent.getComponentType());
        if (transform == null) {
            return;
        }

        applyMovement(transform.getPosition(), spatial);
    }

    public static void applyMovement(Vector3d position, CardSpatialComponent spatial) {
        Vec2f targetPosition = spatial.getTargetPosition();
        position.x = targetPosition.x;
        position.y = spatial.getTargetY();
        position.z = targetPosition.y;
    }

    @Override
    public @Nullable Query<EntityStore> getQuery() {
        return Query.and(CardSpatialComponent.getComponentType());
    }
}
