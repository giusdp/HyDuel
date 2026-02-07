package com.giusdp.htduels.hytale.visibility;

import com.giusdp.htduels.match.zone.ZoneType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CardVisibilityRulesTest {

    @Test
    void ownerSeesHandFaceUp() {
        float pitch = CardVisibilityRules.resolvePitch(ZoneType.HAND, true);
        assertEquals(CardVisibilityRules.FACE_UP, pitch, 0.001f);
    }

    @Test
    void opponentSeesHandFaceDown() {
        float pitch = CardVisibilityRules.resolvePitch(ZoneType.HAND, false);
        assertEquals(CardVisibilityRules.FACE_DOWN, pitch, 0.001f);
    }

    @Test
    void ownerSeesBattlefieldFaceUp() {
        float pitch = CardVisibilityRules.resolvePitch(ZoneType.BATTLEFIELD, true);
        assertEquals(CardVisibilityRules.FACE_UP, pitch, 0.001f);
    }

    @Test
    void opponentSeesBattlefieldFaceUp() {
        float pitch = CardVisibilityRules.resolvePitch(ZoneType.BATTLEFIELD, false);
        assertEquals(CardVisibilityRules.FACE_UP, pitch, 0.001f);
    }

    @Test
    void ownerSeesGraveyardFaceUp() {
        float pitch = CardVisibilityRules.resolvePitch(ZoneType.GRAVEYARD, true);
        assertEquals(CardVisibilityRules.FACE_UP, pitch, 0.001f);
    }

    @Test
    void opponentSeesGraveyardFaceUp() {
        float pitch = CardVisibilityRules.resolvePitch(ZoneType.GRAVEYARD, false);
        assertEquals(CardVisibilityRules.FACE_UP, pitch, 0.001f);
    }

    @Test
    void ownerSeesDeckFaceDown() {
        float pitch = CardVisibilityRules.resolvePitch(ZoneType.DECK, true);
        assertEquals(CardVisibilityRules.FACE_DOWN, pitch, 0.001f);
    }

    @Test
    void opponentSeesDeckFaceDown() {
        float pitch = CardVisibilityRules.resolvePitch(ZoneType.DECK, false);
        assertEquals(CardVisibilityRules.FACE_DOWN, pitch, 0.001f);
    }
}
