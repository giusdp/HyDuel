package com.giusdp.htduels.duel.positioning;

import com.hypixel.hytale.math.Vec2f;

public record BoardLayout(
        Vec2f playerBottomBattlefieldCenter, Vec2f playerTopBattlefieldCenter,
        Vec2f playerBottomHandCenter, Vec2f playerTopHandCenter,
        Vec2f playerBottomDeckPosition, Vec2f playerTopDeckPosition,
        float battlefieldSpacing, float handSpacing,
        float battlefieldCardWidth, float handCardWidth) {
}
