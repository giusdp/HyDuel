package com.giusdp.htduels.presentation.ecs.system;

import com.giusdp.htduels.presentation.ecs.component.BoardLayoutComponent;
import com.giusdp.htduels.presentation.ecs.component.CardComponent;
import com.giusdp.htduels.presentation.ecs.component.CardSpatialComponent;
import com.giusdp.htduels.presentation.layout.BoardLayout;
import com.giusdp.htduels.presentation.layout.CardPositioningService;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.giusdp.htduels.match.zone.ZoneType;
import com.hypixel.hytale.math.Vec2f;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class CardSpatialResolutionSystem extends EntityTickingSystem<EntityStore> {

    @Override
    public void tick(float deltaTime, int index, @NonNull ArchetypeChunk<EntityStore> archetypeChunk,
                     @NonNull Store<EntityStore> store, @NonNull CommandBuffer<EntityStore> commandBuffer) {
        CardComponent cardComponent = archetypeChunk.getComponent(index, CardComponent.getComponentType());
        CardSpatialComponent spatialComponent = archetypeChunk.getComponent(index, CardSpatialComponent.getComponentType());

        if (cardComponent == null || spatialComponent == null) {
            return;
        }

        Ref<EntityStore> duelRef = cardComponent.getDuelEntity();
        if (duelRef == null) {
            return;
        }

        BoardLayoutComponent layoutComponent = store.getComponent(duelRef, BoardLayoutComponent.getComponentType());
        if (layoutComponent == null || layoutComponent.getBoardLayout() == null) {
            return;
        }

        resolvePosition(cardComponent, spatialComponent, layoutComponent.getBoardLayout());
    }

    public static void resolvePosition(CardComponent cc, CardSpatialComponent spatial, BoardLayout boardLayout) {
        if (!spatial.needsResolution(cc)) {
            return;
        }

        Vec2f pos = CardPositioningService.getWorldPosition(
                cc.getZoneType(), cc.getZoneIndex(), cc.getZoneSize(), cc.isOpponentSide(), boardLayout);
        spatial.setTargetPosition(pos);
        spatial.setTargetY(resolveY(cc.getZoneType(), boardLayout));
        spatial.markResolved(cc);
    }

    public static float resolveFacing(CardComponent cc) {
        if (cc.getZoneType() == ZoneType.HAND && cc.isOpponentSide()) {
            return (float) Math.PI;
        }
        return 0f;
    }

    private static float resolveY(ZoneType zoneType, BoardLayout boardLayout) {
        if (zoneType == ZoneType.BATTLEFIELD) {
            return boardLayout.battlefieldYOffset();
        }
        return boardLayout.handYOffset();
    }

    @Override
    public @Nullable Query<EntityStore> getQuery() {
        return Query.and(CardComponent.getComponentType(), CardSpatialComponent.getComponentType());
    }
}
