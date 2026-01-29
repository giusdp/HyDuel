package com.giusdp.htduels.interaction;

import com.giusdp.htduels.PlayerDuelContext;
import com.giusdp.htduels.component.CardComponent;
import com.giusdp.htduels.component.CardDragComponent;
import com.giusdp.htduels.component.DuelComponent;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.event.CardClicked;
import com.giusdp.htduels.duel.event.CardHovered;
import com.giusdp.htduels.duel.event.CardReleased;
import com.giusdp.htduels.duel.event.CardUnhovered;
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

    public static void processClick(PlayerMouseButtonEvent event, PlayerDuelContext session) {
        Vector2f screenPoint = event.getScreenPoint();
        var spatialData = session.getSpatialData();
        Vec2f worldPos = screenToWorld(screenPoint, spatialData);

        Duel duel = getDuel(session);
        if (duel == null) {
            return;
        }

        Duelist clicker = session.getDuelist();
        Store<EntityStore> store = session.getDuelRef().getStore();

        if (event.getMouseButton().state == MouseButtonState.Pressed) {
            var cardUnderMouse = findCardAt(session, worldPos);
            if (cardUnderMouse == null) {
                return;
            }

            CardComponent cardComp = store.getComponent(cardUnderMouse, CardComponent.getComponentType());
            if (cardComp == null || cardComp.getCard().getOwner() != clicker) {
                return;
            }

            CardDragComponent drag = store.getComponent(cardUnderMouse, CardDragComponent.getComponentType());
            if (drag != null) {
                drag.setDragged(true);
                drag.setDragger(session.getPlayer());
            }
            session.setDraggedCard(cardUnderMouse);

            duel.emit(new CardClicked(duel, cardUnderMouse, clicker));
        } else {
            Ref<EntityStore> draggedCard = session.getDraggedCard();
            if (draggedCard == null) {
                return;
            }

            CardDragComponent drag = store.getComponent(draggedCard, CardDragComponent.getComponentType());
            if (drag != null) {
                drag.setDragged(false);
                drag.setDragger(null);
            }
            session.setDraggedCard(null);

            Ref<EntityStore> hoveredCard = hoveredCards.remove(session.getPlayer());
            if (hoveredCard != null) {
                duel.emit(new CardUnhovered(duel, hoveredCard, clicker));
            }

            duel.emit(new CardReleased(duel, draggedCard, clicker));
        }
    }

    public static void processMotion(PlayerMouseMotionEvent event, PlayerDuelContext session) {
        Vector2f screenPoint = event.getScreenPoint();
        Vec2f worldPos = screenToWorld(screenPoint, session.getSpatialData());

        session.setMouseWorldPosition(worldPos);

        if (session.getDraggedCard() != null) {
            return;
        }

        Ref<EntityStore> cardUnderMouse = findCardAt(session, worldPos);
        Ref<EntityStore> previouslyHovered = hoveredCards.get(session.getPlayer());

        if (previouslyHovered == null && cardUnderMouse == null) {
            return;
        }

        Duel duel = getDuel(session);
        if (duel == null) {
            return;
        }

        Duelist viewer = session.getDuelist();

        if (cardUnderMouse == null) {
            duel.emit(new CardUnhovered(duel, previouslyHovered, viewer));
            hoveredCards.remove(session.getPlayer());
        } else if (previouslyHovered == null) {
            duel.emit(new CardHovered(duel, cardUnderMouse, viewer));
            hoveredCards.put(session.getPlayer(), cardUnderMouse);
        }
    }

    public static Vec2f screenToWorld(Vector2f screenPoint, PlayerDuelContext.DuelSpatialData spatialData) {
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

        // Rotate by camera yaw to get world offset
        float cos = (float) Math.cos(cameraYaw);
        float sin = (float) Math.sin(cameraYaw);
        float worldX = (float) cameraPos.x + (localX * cos - localZ * sin);
        float worldZ = (float) cameraPos.z + (localX * sin + localZ * cos);

        return new Vec2f(worldX, worldZ);
    }

    @Nullable
    public static Ref<EntityStore> findCardAt(PlayerDuelContext session, Vec2f worldPos) {
        Store<EntityStore> store = session.getDuelRef().getStore();

        for (Ref<EntityStore> cardRef : session.getCardEntities()) {
            CardComponent card = store.getComponent(cardRef, CardComponent.getComponentType());
            if (card == null) {
                continue;
            }

            TransformComponent transform = store.getComponent(cardRef, TransformComponent.getComponentType());
            BoundingBox bbox = store.getComponent(cardRef, BoundingBox.getComponentType());
            if (transform == null || bbox == null) {
                continue;
            }

            // Check if worldPos is inside card's XZ bounds
            Vector3d pos = transform.getPosition();
            Box box = bbox.getBoundingBox();

            float minX = (float) (pos.x + box.min.x);
            float maxX = (float) (pos.x + box.max.x);
            float minZ = (float) (pos.z + box.min.z);
            float maxZ = (float) (pos.z + box.max.z);

            if (worldPos.x >= minX && worldPos.x <= maxX &&
                    worldPos.y >= minZ && worldPos.y <= maxZ) {
                return cardRef;
            }
        }
        return null;
    }


//    @Nullable
//    public CardComponent getHoveredCard(PlayerRef player) {
//        return hoveredCards.get(player);
//    }
//
//
//    public void clearHoverState(PlayerRef player) {
//        hoveredCards.remove(player);
//    }

    @Nullable
    private static Duel getDuel(PlayerDuelContext session) {
        Store<EntityStore> store = session.getDuelRef().getStore();
        DuelComponent duelComp = store.getComponent(session.getDuelRef(), DuelComponent.getComponentType());
        return duelComp != null ? duelComp.duel : null;
    }
}
