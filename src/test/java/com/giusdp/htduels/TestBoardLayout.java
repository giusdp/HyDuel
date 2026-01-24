package com.giusdp.htduels;

import com.giusdp.htduels.duel.positioning.BoardLayout;
import com.hypixel.hytale.math.Vec2f;

public final class TestBoardLayout {
    private TestBoardLayout() {
    }

    public static BoardLayout create() {
        return new BoardLayout(
                new Vec2f(0, -2),  // playerBottomBattlefieldCenter
                new Vec2f(0, 2),   // playerTopBattlefieldCenter
                new Vec2f(0, -4),  // playerBottomHandCenter
                new Vec2f(0, 4),   // playerTopHandCenter
                new Vec2f(-5, -4), // playerBottomDeckPosition
                new Vec2f(-5, 4),  // playerTopDeckPosition
                1.5f,              // battlefieldSpacing
                1.2f,              // handSpacing
                1.0f,              // battlefieldCardWidth
                0.8f               // handCardWidth
        );
    }
}