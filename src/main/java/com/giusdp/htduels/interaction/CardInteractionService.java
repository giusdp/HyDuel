package com.giusdp.htduels.interaction;

import com.giusdp.htduels.DuelSession;
import com.giusdp.htduels.component.CardComponent;
import com.giusdp.htduels.component.DuelComponent;
import com.giusdp.htduels.duel.Card;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.event.CardClicked;
import com.giusdp.htduels.duelist.Duelist;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.spatial.SpatialData;
import com.hypixel.hytale.math.Vec2f;
import com.hypixel.hytale.math.shape.Box;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.protocol.Position;
import com.hypixel.hytale.protocol.Vector2f;
import com.hypixel.hytale.server.core.event.events.player.PlayerMouseButtonEvent;
import com.hypixel.hytale.server.core.modules.collision.CollisionMath;
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

    private static final Map<PlayerRef, CardComponent> hoveredCards = new HashMap<>();

    public static void processClick(PlayerMouseButtonEvent event, DuelSession session) {
        Vector2f screenPoint = event.getScreenPoint();
        var spatialData = session.getSpatialData();
        Vec2f worldPos = screenToWorld(screenPoint, spatialData);

        CardComponent cardUnderMouse = findCardAt(session, worldPos);

        if (cardUnderMouse == null) {
            return;
        }

        Store<EntityStore> store = session.getDuelRef().getStore();
        Duel duel = getDuel(store, session.getDuelRef());
        if (duel == null) {
            return;
        }

        Card card = cardUnderMouse.getCard();


        Duelist clicker = duel.duelist1; // TODO: Determine which duelist clicked

        duel.emit(new CardClicked(duel, card, clicker));
    }


    public static Vec2f screenToWorld(Vector2f screenPoint, DuelSession.DuelSpatialData spatialData) {
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
    public static CardComponent findCardAt(DuelSession session, Vec2f worldPos) {
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
                return card;
            }
        }
        return null;
    }

//    public static void processMotion(PlayerMouseMotionEvent event, DuelSession session) {
//        Vector2f screenPoint = event.getScreenPoint();
//        Vec2f worldPos = screenToWorld(screenPoint, session.getCameraPos(),
//                session.getCameraYaw(), session.getCardY());
//
//        CardComponent cardUnderMouse = findCardAt(worldPos, session);
//        CardComponent previouslyHovered = hoveredCards.get(session.getPlayer());
//
//        // Check if hover state changed
//        if (cardUnderMouse != previouslyHovered) {
//            Store<EntityStore> store = session.getDuelRef().getStore();
//            Duel duel = getDuel(store, session.getDuelRef());
//            if (duel == null) {
//                return;
//            }
//
//            Duelist viewer = duel.duelist2; // TODO: Determine which duelist is viewing
//
//            // Emit unhover event for previous card
//            if (previouslyHovered != null) {
//                Card prevCard = previouslyHovered.getCard();
//                duel.emit(new CardUnhovered(duel, prevCard, viewer));
//                LOGGER.atInfo().log("Card unhovered: %s", prevCard.getAsset().id());
//            }
//
//            // Emit hover event for new card
//            if (cardUnderMouse != null) {
//                Card newCard = cardUnderMouse.getCard();
//                duel.emit(new CardHovered(duel, newCard, viewer));
//                LOGGER.atInfo().log("Card hovered: %s", newCard.getAsset().id());
//                hoveredCards.put(session.getPlayer(), cardUnderMouse);
//            } else {
//                hoveredCards.remove(session.getPlayer());
//            }
//        }
//    }


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
    private static Duel getDuel(Store<EntityStore> store, Ref<EntityStore> duelRef) {
        DuelComponent duelComp = store.getComponent(duelRef, DuelComponent.getComponentType());
        return duelComp != null ? duelComp.duel : null;
    }
}
