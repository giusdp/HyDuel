package com.giusdp.htduels.interaction;

import com.giusdp.htduels.DuelistContext;
import com.giusdp.htduels.component.BoardLayoutComponent;
import com.giusdp.htduels.component.CardComponent;
import com.giusdp.htduels.component.CardDragComponent;
import com.giusdp.htduels.component.CardHoverComponent;
import com.giusdp.htduels.component.DuelComponent;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.event.PlayCard;
import com.giusdp.htduels.duel.positioning.BoardLayout;
import com.giusdp.htduels.duel.zone.ZoneType;
import com.giusdp.htduels.duelist.Duelist;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.Vec2f;
import com.hypixel.hytale.math.shape.Box;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.protocol.MouseButtonState;
import com.hypixel.hytale.protocol.Position;
import com.hypixel.hytale.protocol.Vector2f;
import com.hypixel.hytale.server.core.event.events.player.PlayerMouseButtonEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerMouseMotionEvent;
import com.hypixel.hytale.server.core.modules.entity.component.BoundingBox;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class CardInteractionService {
    private static final float FOV_RADIANS = (float) Math.toRadians(80);
    private static final float ASPECT_RATIO = 16f / 9f;

    private static final Map<PlayerRef, Ref<EntityStore>> hoveredCards = new HashMap<>();

    public static void clearHoveredCard(PlayerRef playerRef) {
        hoveredCards.remove(playerRef);
    }

    public static void processClick(PlayerMouseButtonEvent event, DuelistContext ctx) {
        Vector2f screenPoint = event.getScreenPoint();
        var spatialData = ctx.getSpatialData();
        Vec2f worldPos = screenToWorld(screenPoint, spatialData);

        Store<EntityStore> store = ctx.getDuelRef().getStore();

        if (event.getMouseButton().state == MouseButtonState.Pressed) {
            var cardUnderMouse = findCardAt(ctx, worldPos);
            if (cardUnderMouse == null) {
                return;
            }

            CardComponent cardComp = store.getComponent(cardUnderMouse, CardComponent.getComponentType());
            if (cardComp == null || cardComp.getCard().getOwner() != ctx.getDuelist()) {
                return;
            }

            if (cardComp.getCard().getCurrentZoneType() != ZoneType.HAND) {
                return;
            }

            CardDragComponent drag = store.getComponent(cardUnderMouse, CardDragComponent.getComponentType());
            if (drag != null) {
                drag.setDragged(true);
                drag.setDragger(ctx.getPlayerRef());
            }
            ctx.setDraggedCard(cardUnderMouse);
        } else {
            Ref<EntityStore> draggedCard = ctx.getDraggedCard();
            if (draggedCard == null) {
                return;
            }

            CardDragComponent drag = store.getComponent(draggedCard, CardDragComponent.getComponentType());
            if (drag != null) {
                drag.setDragged(false);
                drag.setDragger(null);
            }
            ctx.setDraggedCard(null);

            Ref<EntityStore> hoveredCard = hoveredCards.remove(ctx.getPlayerRef());
            if (hoveredCard != null) {
                setHovered(store, hoveredCard, false);
            }

            BoardLayout boardLayout = getBoardLayout(store, ctx.getDuelRef());
            Duel duel = getDuel(store, ctx.getDuelRef());
            if (boardLayout == null || duel == null) {
                return;
            }
            Duelist duelist = ctx.getDuelist();
            if (boardLayout.isInBattlefieldZone(worldPos, duelist.isBottomPlayer())) {
                CardComponent cardComp = store.getComponent(draggedCard, CardComponent.getComponentType());
                if (cardComp != null) {
                    duel.emit(new PlayCard(duel, duelist, cardComp.getCard()));
                }
            }
        }
    }

    public static void processMotion(PlayerMouseMotionEvent event, DuelistContext ctx) {
        Vector2f screenPoint = event.getScreenPoint();
        Vec2f worldPos = screenToWorld(screenPoint, ctx.getSpatialData());

        ctx.setMouseWorldPosition(worldPos);

        if (ctx.getDraggedCard() != null) {
            return;
        }

        Ref<EntityStore> cardUnderMouse = findCardAt(ctx, worldPos);
        Ref<EntityStore> previouslyHovered = hoveredCards.get(ctx.getPlayerRef());

        if (previouslyHovered == null && cardUnderMouse == null) {
            return;
        }

        Store<EntityStore> store = ctx.getDuelRef().getStore();

        // Same card — nothing to do
        if (cardUnderMouse != null && cardUnderMouse.equals(previouslyHovered)) {
            return;
        }

        // Unhover the previous card if there was one
        if (previouslyHovered != null) {
            setHovered(store, previouslyHovered, false);
            hoveredCards.remove(ctx.getPlayerRef());
        }

        // Hover the new card if it's in hand
        if (cardUnderMouse != null && isInHand(store, cardUnderMouse)) {
            setHovered(store, cardUnderMouse, true);
            hoveredCards.put(ctx.getPlayerRef(), cardUnderMouse);
        }
    }

    public static Vec2f screenToWorld(Vector2f screenPoint, DuelistContext.DuelSpatialData spatialData) {
        Position cameraPos = spatialData.cameraPos();
        float cameraYaw = spatialData.cameraYaw();
        float cardY = spatialData.cardY();

        // Calculate visible area at card height
        float cameraHeight = (float) (cameraPos.y - cardY);
        float halfHeight = cameraHeight * (float) Math.tan(FOV_RADIANS / 2);
        float halfWidth = halfHeight * ASPECT_RATIO;

        // Screen coords to camera-local offset
        float localX = screenPoint.x * halfWidth;
        float localZ = screenPoint.y * halfHeight;

        // Rotate by camera yaw to get world offset (negate yaw to match Rotation.rotateY convention)
        float cos = (float) Math.cos(cameraYaw);
        float sin = (float) Math.sin(cameraYaw);
        float worldX = (float) cameraPos.x + (localX * cos + localZ * sin);
        float worldZ = (float) cameraPos.z + (-localX * sin + localZ * cos);

        return new Vec2f(worldX, worldZ);
    }

    @Nullable
    public static Ref<EntityStore> findCardAt(DuelistContext ctx, Vec2f worldPos) {
        Store<EntityStore> store = ctx.getDuelRef().getStore();

        for (Ref<EntityStore> cardRef : ctx.getCardEntities()) {
            CardComponent card = store.getComponent(cardRef, CardComponent.getComponentType());
            if (card == null) {
                continue;
            }

            TransformComponent transform = store.getComponent(cardRef, TransformComponent.getComponentType());
            BoundingBox bbox = store.getComponent(cardRef, BoundingBox.getComponentType());
            if (transform == null || bbox == null) {
                continue;
            }

            // Transform world position into card-local space to handle rotated bounding boxes
            Vector3d pos = transform.getPosition();
            float dx = worldPos.x - (float) pos.x;
            float dz = worldPos.y - (float) pos.z;

            float yaw = transform.getRotation().getYaw();
            float cos = (float) Math.cos(-yaw);
            float sin = (float) Math.sin(-yaw);
            float localX = dx * cos - dz * sin;
            float localZ = dx * sin + dz * cos;

            Box box = bbox.getBoundingBox();
            if (localX >= box.min.x && localX <= box.max.x &&
                    localZ >= box.min.z && localZ <= box.max.z) {
                return cardRef;
            }
        }
        return null;
    }


    private static void setHovered(Store<EntityStore> store, Ref<EntityStore> cardRef, boolean hovered) {
        CardHoverComponent hover = store.getComponent(cardRef, CardHoverComponent.getComponentType());
        if (hover != null) {
            hover.setHovered(hovered);
        }
    }

    @Nullable
    private static Duel getDuel(Store<EntityStore> store, Ref<EntityStore> duelRef) {
        DuelComponent duelComp = store.getComponent(duelRef, DuelComponent.getComponentType());
        return duelComp != null ? duelComp.duel : null;
    }

    @Nullable
    private static BoardLayout getBoardLayout(Store<EntityStore> store, Ref<EntityStore> duelRef) {
        BoardLayoutComponent layoutComp = store.getComponent(duelRef, BoardLayoutComponent.getComponentType());
        return layoutComp != null ? layoutComp.getBoardLayout() : null;
    }

    private static boolean isInHand(Store<EntityStore> store, Ref<EntityStore> cardRef) {
        CardComponent comp = store.getComponent(cardRef, CardComponent.getComponentType());
        return comp != null && comp.getCard().getCurrentZoneType() == ZoneType.HAND;
    }
}
