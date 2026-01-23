package com.giusdp.htduels.event;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.event.events.player.PlayerMouseMotionEvent;

import javax.annotation.Nonnull;

public class CardMotionHandler {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private static final double FOV_RADIANS = Math.toRadians(80);
    private static final double ASPECT_RATIO = 16.0 / 9.0;

    public static void handleMouseMotion(@Nonnull PlayerMouseMotionEvent event) {
        // TODO: Implement card dragging properly
//        if (!BoardMouseHandler.clickedOnCard) return;
//
//        if (SpawnCardCommand.cardRef == null || BoardInteraction.cameraPos == null) {
//            return;
//        }
//
//        Vector2f screenPoint = event.getScreenPoint();
//
//        // Convert screen coords to world position (same as BoardMouseHandler)
//        double cardY = SpawnCardCommand.spawnPosition.getY();
//        double cameraY = BoardInteraction.cameraPos.y;
//        double distance = cameraY - cardY;
//
//        double halfH = distance * Math.tan(FOV_RADIANS * 0.5);
//        double halfW = halfH * ASPECT_RATIO;
//
//        double worldX = BoardInteraction.cameraPos.x + screenPoint.x * halfW;
//        double worldZ = BoardInteraction.cameraPos.z + screenPoint.y * halfH;
//
//        // Update card position
//        var store = SpawnCardCommand.cardRef.getStore();
//        var transform = store.getComponent(SpawnCardCommand.cardRef, TransformComponent.getComponentType());
//        if (transform != null) {
//            transform.setPosition(new Vector3d(worldX, cardY, worldZ));
//        }
    }

}
