package com.giusdp.htduels.duel.positioning;

import com.giusdp.htduels.duel.Card;
import com.hypixel.hytale.math.Vec2f;

@FunctionalInterface
public interface CardPositionFunction {
    Vec2f compute(Card card, BoardLayout board);
}
