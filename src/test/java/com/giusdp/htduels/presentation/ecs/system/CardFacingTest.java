package com.giusdp.htduels.presentation.ecs.system;

import com.giusdp.htduels.catalog.CardAsset;
import com.giusdp.htduels.presentation.ecs.component.CardComponent;
import com.giusdp.htduels.match.Card;
import com.giusdp.htduels.match.CardId;
import com.giusdp.htduels.match.zone.ZoneType;
import com.giusdp.htduels.match.HumanTurnStrategy;
import com.giusdp.htduels.match.Duelist;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardFacingTest {

    private CardComponent createHandComponent(boolean opponentSide) {
        return new CardComponent(CardId.generate(), null, ZoneType.HAND, 0, 1, opponentSide);
    }

    private CardComponent createBattlefieldComponent(boolean opponentSide) {
        return new CardComponent(CardId.generate(), null, ZoneType.BATTLEFIELD, 0, 1, opponentSide);
    }

    @Test
    void topPlayerHandCardIsFaceUp() {
        CardComponent cc = createHandComponent(false);
        assertEquals(0f, CardSpatialResolutionSystem.resolveFacing(cc), 0.001);
    }

    @Test
    void bottomPlayerHandCardIsFaceDown() {
        CardComponent cc = createHandComponent(true);
        assertEquals((float) Math.PI, CardSpatialResolutionSystem.resolveFacing(cc), 0.001);
    }

    @Test
    void topPlayerBattlefieldCardIsFaceUp() {
        CardComponent cc = createBattlefieldComponent(false);
        assertEquals(0f, CardSpatialResolutionSystem.resolveFacing(cc), 0.001);
    }

    @Test
    void bottomPlayerBattlefieldCardIsFaceUp() {
        CardComponent cc = createBattlefieldComponent(true);
        assertEquals(0f, CardSpatialResolutionSystem.resolveFacing(cc), 0.001);
    }
}
