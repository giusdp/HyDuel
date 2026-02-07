package com.giusdp.htduels.hytale.visibility;

import com.giusdp.htduels.match.zone.ZoneType;

public final class CardVisibilityRules {
    public static final float FACE_UP = 0f;
    public static final float FACE_DOWN = (float) Math.PI;

    private CardVisibilityRules() {
    }

    public static float resolvePitch(ZoneType zone, boolean isOwner) {
        return switch (zone) {
            case HAND -> isOwner ? FACE_UP : FACE_DOWN;
            case BATTLEFIELD, GRAVEYARD -> FACE_UP;
            case DECK -> FACE_DOWN;
        };
    }
}
