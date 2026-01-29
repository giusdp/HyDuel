package com.giusdp.htduels.system;

import com.giusdp.htduels.component.CardHoverComponent;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class CardHoverSystem extends EntityTickingSystem<EntityStore> {
    static final float HOVER_OFFSET = 0.05f;

    @Override
    public void tick(float deltaTime, int index, @NonNull ArchetypeChunk<EntityStore> archetypeChunk,
                     @NonNull Store<EntityStore> store, @NonNull CommandBuffer<EntityStore> commandBuffer) {
        CardHoverComponent hoverComponent = archetypeChunk.getComponent(index, CardHoverComponent.getComponentType());
        if (hoverComponent == null) {
            return;
        }

        TransformComponent transform = archetypeChunk.getComponent(index, TransformComponent.getComponentType());
        if (transform == null) {
            return;
        }

        applyHover(hoverComponent, transform.getPosition());
    }

    public static void applyHover(CardHoverComponent hoverComponent, Vector3d position) {
        float targetY = hoverComponent.getOriginalY();

        if (hoverComponent.isHovered()) {
            targetY += HOVER_OFFSET;
        }

        if (position.y != targetY) {
            position.y = targetY;
        }
    }

    @Override
    public @Nullable Query<EntityStore> getQuery() {
        return Query.and(CardHoverComponent.getComponentType());
    }
}
