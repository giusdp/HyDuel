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
        float handCardWidth) {

    public Vec2f toWorldPosition(float localX, float localZ) {
        Vector3f local = new Vector3f(localX, 0, localZ);
        Vector3f rotated = rotation.rotateY(local, new Vector3f());
        return new Vec2f(boardOrigin.x + rotated.x, boardOrigin.y + rotated.z);
    }
}
