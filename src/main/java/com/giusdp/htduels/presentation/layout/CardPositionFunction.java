package com.giusdp.htduels.presentation.layout;

import com.hypixel.hytale.math.Vec2f;

@FunctionalInterface
public interface CardPositionFunction {
    Vec2f compute(int index, int zoneSize, boolean opponentSide, BoardLayout board);
}
