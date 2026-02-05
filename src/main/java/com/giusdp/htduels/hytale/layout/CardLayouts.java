package com.giusdp.htduels.hytale.layout;

import com.hypixel.hytale.math.Vec2f;

public final class CardLayouts {

    private CardLayouts() {
    }

    public static Vec2f battlefield(int index, int zoneSize, boolean opponentSide, BoardLayout board) {
        float localX = (index - (zoneSize - 1) / 2.0f) * board.battlefieldSpacing();
        float localZ = opponentSide
                ? -board.playerBattlefieldDepth()
                : board.opponentBattlefieldDepth();

        return board.toWorldPosition(localX, localZ);
    }

    public static Vec2f hand(int index, int zoneSize, boolean opponentSide, BoardLayout board) {
        float localX = (index - (zoneSize - 1) / 2.0f) * board.handSpacing();
        float localZ = opponentSide
                ? -board.playerHandDepth()
                : board.opponentHandDepth();

        return board.toWorldPosition(localX, localZ);
    }

    public static Vec2f deck(int index, int zoneSize, boolean opponentSide, BoardLayout board) {
        float localZ = opponentSide
                ? -board.playerHandDepth()
                : board.opponentHandDepth();

        return board.toWorldPosition(-board.deckOffsetX(), localZ);
    }

    public static Vec2f graveyard(int index, int zoneSize, boolean opponentSide, BoardLayout board) {
        float localZ = opponentSide
                ? -board.playerHandDepth()
                : board.opponentHandDepth();

        // Graveyard on the opposite side of deck
        return board.toWorldPosition(board.deckOffsetX(), localZ);
    }
}
