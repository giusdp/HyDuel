package com.giusdp.htduels.effects;

import com.giusdp.htduels.duel.Card;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.Hand;

import java.util.function.Function;

public class EffectRepo {

    public static Effect fillHands() {
        return duel -> {
            for (Hand hand : duel.playerHands) {
                for (int i = 0; i < 5; i++) {
                    hand.cards.add(new Card());
                }
            }
        };
    }
}
