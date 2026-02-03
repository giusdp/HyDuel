package com.giusdp.htduels;

import com.giusdp.htduels.presentation.layout.BoardLayout;
import com.hypixel.hytale.math.Vec2f;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.Rotation;

public final class TestBoardLayout {
    private TestBoardLayout() {
    }

    /**
     * Creates a test BoardLayout with default rotation (None/North).
     */
    public static BoardLayout create() {
        return create(Rotation.None);
    }

    /**
     * Creates a test BoardLayout with the specified rotation.
     */
    public static BoardLayout create(Rotation rotation) {
        return new BoardLayout(
                new Vec2f(0, 0),  // boardOrigin (center)
                rotation,
                2.0f,   // playerBattlefieldDepth
                2.0f,   // opponentBattlefieldDepth
                4.0f,   // playerHandDepth
                4.0f,   // opponentHandDepth
                5.0f,   // deckOffsetX
                1.5f,   // battlefieldSpacing
                1.2f,   // handSpacing
                1.0f,   // battlefieldCardWidth
                0.8f,   // handCardWidth
                1.2f,   // handYOffset
                0.8f    // battlefieldYOffset
        );
    }
}
