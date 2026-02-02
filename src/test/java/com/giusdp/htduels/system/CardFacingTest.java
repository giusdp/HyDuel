package com.giusdp.htduels.system;

import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.component.CardComponent;
import com.giusdp.htduels.duel.Card;
import com.giusdp.htduels.duel.CardId;
import com.giusdp.htduels.duel.zone.ZoneType;
import com.giusdp.htduels.duelist.PlayerTurnStrategy;
import com.giusdp.htduels.duelist.Duelist;
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
