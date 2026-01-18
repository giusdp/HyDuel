package com.giusdp.htduels.events;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.MouseButtonState;
import com.hypixel.hytale.protocol.MouseButtonType;
import com.hypixel.hytale.protocol.Vector2f;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.event.events.player.PlayerMouseButtonEvent;

import javax.annotation.Nonnull;

public class BoardMouseHandler {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public static void handleMouseClick(@Nonnull PlayerMouseButtonEvent event) {
        if (event.getMouseButton().state != MouseButtonState.Released) {
            return;
        }
        Vector2f screenPoint = event.getScreenPoint();

        LOGGER.atInfo().log("Mouse click event received - button state: Released");

        MouseButtonType buttonType = event.getMouseButton().mouseButtonType;

        event.getPlayer().sendMessage(Message.raw(
                String.format("Clicked %s at (%f, %f) ", buttonType, screenPoint.x, screenPoint.y)
        ));

        LOGGER.atInfo().log("Player clicked but nothing was targeted");
    }

}
