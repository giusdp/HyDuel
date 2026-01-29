package com.giusdp.htduels.duel.positioning;

import com.hypixel.hytale.math.Vec2f;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.Rotation;

public record BoardLayout(
        Vec2f boardOrigin,
        Rotation rotation,
        float playerBattlefieldDepth,
        float opponentBattlefieldDepth,
        float playerHandDepth,
        float opponentHandDepth,
        float deckOffsetX,
        float battlefieldSpacing,
        float handSpacing,
        float battlefieldCardWidth,
        float handCardWidth,
        float handYOffset,
        float battlefieldYOffset) {

    public Vec2f toWorldPosition(float localX, float localZ) {
        Vector3f local = new Vector3f(localX, 0, localZ);
        Vector3f rotated = rotation.rotateY(local, new Vector3f());
        return new Vec2f(boardOrigin.x + rotated.x, boardOrigin.y + rotated.z);
    }

    public Vec2f toLocalPosition(Vec2f worldPos) {
        float dx = worldPos.x - boardOrigin.x;
        float dz = worldPos.y - boardOrigin.y;
        Rotation inverse = inverseRotation();
        Vector3f rotated = inverse.rotateY(new Vector3f(dx, 0, dz), new Vector3f());
        return new Vec2f(rotated.x, rotated.z);
    }

    public boolean isInBattlefieldZone(Vec2f worldPos, boolean isBottomPlayer) {
        Vec2f local = toLocalPosition(worldPos);
        float handDepth = isBottomPlayer ? playerHandDepth : opponentHandDepth;
        float sign = isBottomPlayer ? -1f : 1f;

        float minZ = 0;
        float maxZ = sign * handDepth;

        float lo = Math.min(minZ, maxZ);
        float hi = Math.max(minZ, maxZ);

        return local.y >= lo && local.y <= hi;
    }

    private Rotation inverseRotation() {
        return switch (rotation) {
            case None -> Rotation.None;
            case Ninety -> Rotation.TwoSeventy;
            case OneEighty -> Rotation.OneEighty;
            case TwoSeventy -> Rotation.Ninety;
        };
    }
}
