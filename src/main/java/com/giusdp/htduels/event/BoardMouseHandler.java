package com.giusdp.htduels.event;

import com.giusdp.htduels.command.SpawnCardCommand;
import com.giusdp.htduels.interaction.BoardInteraction;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.shape.Box;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.protocol.MouseButtonState;
import com.hypixel.hytale.protocol.Vector2f;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.event.events.player.PlayerMouseButtonEvent;

import javax.annotation.Nonnull;

public class BoardMouseHandler {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private static final double FOV_RADIANS = Math.toRadians(80);
    private static final double ASPECT_RATIO = 16.0 / 9.0;

    public static void handleMouseClick(@Nonnull PlayerMouseButtonEvent event) {
        if (event.getMouseButton().state != MouseButtonState.Released) {
            return;
        }

        Vector2f screenPoint = event.getScreenPoint();
        Vector3d cardPos = SpawnCardCommand.spawnPosition;
        Box box = SpawnCardCommand.spawnBoundingBox;

        if (cardPos == null || box == null) {
            return;
        }

        // Calculate visible area at card height
        double cameraY = BoardInteraction.cameraPos.y;
        double cardY = cardPos.getY();
        double distance = cameraY - cardY;

        // Half the visible height/width at card level
        double halfH = distance * Math.tan(FOV_RADIANS * 0.5);
        double halfW = halfH * ASPECT_RATIO;

        // Convert screen coords (-1 to 1) to world position
        double worldX = BoardInteraction.cameraPos.x + screenPoint.x * halfW;
        double worldZ = BoardInteraction.cameraPos.z + screenPoint.y * halfH;

        LOGGER.atInfo().log("Click: screen (%.2f, %.2f) -> world (%.2f, %.2f)",
            screenPoint.x, screenPoint.y, worldX, worldZ);

        // 2D bounds check (top-down view)
        double minX = cardPos.x + box.min.x;
        double maxX = cardPos.x + box.max.x;
        double minZ = cardPos.z + box.min.z;
        double maxZ = cardPos.z + box.max.z;

        boolean hit = worldX >= minX && worldX <= maxX && worldZ >= minZ && worldZ <= maxZ;

        LOGGER.atInfo().log("Card bounds X:[%.2f, %.2f] Z:[%.2f, %.2f] - Hit: %b",
            minX, maxX, minZ, maxZ, hit);
        event.getPlayer().sendMessage(Message.raw("Card hit: " + hit));
    }
}
