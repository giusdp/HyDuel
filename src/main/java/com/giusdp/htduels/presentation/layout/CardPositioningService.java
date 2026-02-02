package com.giusdp.htduels.presentation.layout;

import com.giusdp.htduels.match.zone.ZoneType;
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

    public static Vec2f getWorldPosition(ZoneType zoneType, int index, int zoneSize, boolean opponentSide, BoardLayout board) {
        CardPositionFunction fn = FUNCTIONS.get(zoneType);

        if (fn == null) {
            throw new IllegalStateException("No layout function for zone " + zoneType);
        }

        return fn.compute(index, zoneSize, opponentSide, board);
    }
}
