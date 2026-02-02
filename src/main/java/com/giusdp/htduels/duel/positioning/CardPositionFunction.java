package com.giusdp.htduels.duel.positioning;

import com.hypixel.hytale.math.Vec2f;

@FunctionalInterface
public interface CardPositionFunction {
    Vec2f compute(int index, int zoneSize, boolean opponentSide, BoardLayout board);
}
