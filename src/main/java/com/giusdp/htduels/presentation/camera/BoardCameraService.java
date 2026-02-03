package com.giusdp.htduels.presentation.camera;

import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.*;
import com.hypixel.hytale.protocol.packets.camera.SetServerCamera;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.Rotation;
import com.hypixel.hytale.server.core.universe.PlayerRef;

public final class BoardCameraService {

    private static final double Y_OFFSET = 1.75;
    private static final float CAMERA_PITCH = (float) Math.toRadians(-90.0f);

    private BoardCameraService() {}

    public static void activate(PlayerRef playerRef, Position cameraPos, float yaw) {
        ServerCameraSettings settings = createBoardCameraSettings(cameraPos, yaw);
        playerRef.getPacketHandler().writeNoCache(new SetServerCamera(ClientCameraView.Custom, true, settings));
    }

    public static void resetToFirstPerson(PlayerRef playerRef) {
        SetServerCamera serverCamera = new SetServerCamera(ClientCameraView.FirstPerson, false, null);
        playerRef.getPacketHandler().writeNoCache(serverCamera);
    }

    public static Position calculateCameraPosition(Vector3i boardPosition, Rotation rotation) {
        double centerX = boardPosition.x;
        double centerY = boardPosition.y + Y_OFFSET;
        double centerZ = boardPosition.z;

        switch (rotation) {
            case None -> centerZ += 0.5;
            case Ninety -> {
                centerX += 0.5;
                centerZ += 1;
            }
            case OneEighty -> {
                centerX += 1;
                centerZ += 0.5;
            }
            case TwoSeventy -> centerX += 0.5;
        }

        return new Position(centerX, centerY, centerZ);
    }

    public static ServerCameraSettings createBoardCameraSettings(Position cameraPosition, float yaw) {
        ServerCameraSettings settings = new ServerCameraSettings();

        settings.positionType = PositionType.Custom;
        settings.position = cameraPosition;

        settings.rotationType = RotationType.Custom;
        settings.rotation = new Direction(yaw, CAMERA_PITCH, 0);

        settings.movementMultiplier = new com.hypixel.hytale.protocol.Vector3f(0, 0, 0);
        settings.applyMovementType = ApplyMovementType.Position;

        settings.displayCursor = true;
        settings.displayReticle = false;
        settings.skipCharacterPhysics = true;
        settings.isFirstPerson = false;

        settings.sendMouseMotion = true;
        settings.mouseInputType = MouseInputType.LookAtTargetEntity;
        settings.planeNormal = new com.hypixel.hytale.protocol.Vector3f(0, 1, 0);
        return settings;
    }
}
