package com.giusdp.htduels.presentation.ecs.system;

import com.giusdp.htduels.presentation.DuelPresentationService;
import com.giusdp.htduels.presentation.DuelistSessionManager;
import com.giusdp.htduels.presentation.ecs.component.CardDragComponent;
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

public class CardDragSystem extends EntityTickingSystem<EntityStore> {

    private final DuelPresentationService presentationService;

    public CardDragSystem(DuelPresentationService presentationService) {
        this.presentationService = presentationService;
    }

    @Override
    public void tick(float deltaTime, int index, @NonNull ArchetypeChunk<EntityStore> archetypeChunk,
                     @NonNull Store<EntityStore> store, @NonNull CommandBuffer<EntityStore> commandBuffer) {
        CardDragComponent dragComponent = archetypeChunk.getComponent(index, CardDragComponent.getComponentType());
        if (dragComponent == null || !dragComponent.isDragged()) {
            return;
        }

        TransformComponent transform = archetypeChunk.getComponent(index, TransformComponent.getComponentType());
        if (transform == null) {
            return;
        }

        var dragger = dragComponent.getDragger();
        if (dragger == null) {
            return;
        }

        DuelistSessionManager session = presentationService.getSession(dragger);
        if (session == null) {
            return;
        }

        Vec2f mousePos = session.getMouseWorldPosition();
        if (mousePos == null) {
            return;
        }

        applyDrag(transform.getPosition(), mousePos);
    }

    public static void applyDrag(Vector3d position, Vec2f mouseWorldPosition) {
        position.x = mouseWorldPosition.x;
        position.z = mouseWorldPosition.y;
    }

    @Override
    public @Nullable Query<EntityStore> getQuery() {
        return Query.and(CardDragComponent.getComponentType());
    }
}
