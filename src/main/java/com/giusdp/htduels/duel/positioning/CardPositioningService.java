package com.giusdp.htduels.duel.positioning;

import com.giusdp.htduels.duel.Card;
import com.giusdp.htduels.duel.zone.ZoneType;
import com.hypixel.hytale.math.Vec2f;

import java.util.Map;

public class CardPositioningService {
    private static final Map<ZoneType, CardPositionFunction> FUNCTIONS =
            Map.of(
                    ZoneType.BATTLEFIELD, CardLayouts::battlefield,
                    ZoneType.HAND, CardLayouts::hand,
                    ZoneType.DECK, CardLayouts::deck,
                    ZoneType.GRAVEYARD, CardLayouts::graveyard
            );

    private CardPositioningService() {
    }

    public static Vec2f getWorldPosition(Card card, BoardLayout board) {
        CardPositionFunction fn = FUNCTIONS.get(card.getZone().getType());

        if (fn == null) {
            throw new IllegalStateException("No layout function for zone " + card.getZone().getType());
        }

        return fn.compute(card, board);
    }
}
