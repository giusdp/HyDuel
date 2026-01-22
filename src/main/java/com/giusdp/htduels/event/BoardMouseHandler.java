package com.giusdp.htduels.event;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.protocol.ClientCameraView;
import com.hypixel.hytale.protocol.MouseButtonState;
import com.hypixel.hytale.protocol.MouseButtonType;
import com.hypixel.hytale.protocol.Vector2f;
import com.hypixel.hytale.protocol.packets.camera.SetServerCamera;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerMouseButtonEvent;
import com.hypixel.hytale.server.core.modules.collision.CollisionMath;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.UseEntityInteraction;
import com.hypixel.hytale.server.core.util.TargetUtil;

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

        event.getPlayer().sendMessage(
                Message.raw(String.format("Clicked %s at (%f, %f) ", buttonType, screenPoint.x, screenPoint.y)));
        LOGGER.atInfo().log(event.getScreenPoint().toString());
        var targetEntity = event.getTargetEntity();
        Player p  = event.getPlayer();
        LOGGER.atInfo().log(p.toString());
        LOGGER.atInfo().log(""+p.getViewRadius());
        LOGGER.atInfo().log(""+p.getClientViewRadius());
        if (targetEntity == null) {
            LOGGER.atInfo().log("No target entity found under mouse click.");
            return;
        }

        LOGGER.atInfo().log(targetEntity.toString());


//        CollisionMath.intersectRayAABB()
    }

//    Vector3f screenToWorldTopDown(
//            Vector2f screenPos, Camera cam, float groundY
//    ) {
//        float halfH = cam.position.y * (float)Math.tan(cam.fov * 0.5f);
//        float halfW = halfH * cam.aspect;
//
//        return new Vector3(
//                cam.position.x + screenPos.x * halfW,
//                groundY,
//                cam.position.z + screenPos.y * halfH
//        );
//    }

}
