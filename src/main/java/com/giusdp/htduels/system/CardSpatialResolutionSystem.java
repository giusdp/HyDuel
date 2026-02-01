package com.giusdp.htduels.system;

import com.giusdp.htduels.component.BoardLayoutComponent;
import com.giusdp.htduels.component.CardComponent;
import com.giusdp.htduels.component.CardSpatialComponent;
import com.giusdp.htduels.duel.Card;
import com.giusdp.htduels.duel.positioning.BoardLayout;
import com.giusdp.htduels.duel.positioning.CardPositioningService;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.giusdp.htduels.duel.zone.ZoneType;
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

        resolvePosition(cardComponent.getCard(), spatialComponent, layoutComponent.getBoardLayout());
    }

    public static void resolvePosition(Card card, CardSpatialComponent spatial, BoardLayout boardLayout) {
        if (!spatial.needsResolution(card)) {
            return;
        }

        Vec2f pos = CardPositioningService.getWorldPosition(card, boardLayout);
        spatial.setTargetPosition(pos);
        spatial.setTargetY(resolveY(card, boardLayout));
        spatial.markResolved(card);
    }

    public static float resolveFacing(Card card) {
        if (card.getCurrentZoneType() == ZoneType.HAND && card.getOwner() != null && card.getOwner().isOpponentSide()) {
            return (float) Math.PI;
        }
        return 0f;
    }

    private static float resolveY(Card card, BoardLayout boardLayout) {
        ZoneType zone = card.getCurrentZoneType();
        if (zone == ZoneType.BATTLEFIELD) {
            return boardLayout.battlefieldYOffset();
        }
        return boardLayout.handYOffset();
    }

    @Override
    public @Nullable Query<EntityStore> getQuery() {
        return Query.and(CardComponent.getComponentType(), CardSpatialComponent.getComponentType());
    }
}
