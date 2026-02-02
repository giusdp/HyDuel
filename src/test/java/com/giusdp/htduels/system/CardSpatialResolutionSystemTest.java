package com.giusdp.htduels.system;

import com.giusdp.htduels.FakeCardRepo;

import com.giusdp.htduels.TestBoardLayout;
import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.component.CardSpatialComponent;
import com.giusdp.htduels.duel.Card;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.positioning.BoardLayout;
import com.giusdp.htduels.duelist.Bot;
import com.giusdp.htduels.duelist.DuelPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardSpatialResolutionSystemTest {
    Duel duel;
    BoardLayout boardLayout;

    @BeforeEach
    void setup() {
        boardLayout = TestBoardLayout.create();
        duel = Duel.builder()
                .cardRepo(new FakeCardRepo())
                .addDuelist(new DuelPlayer(), true)
                .addDuelist(new Bot(), false)
                .build();
    }

    @Test
    void resolvesPositionWhenDirty() {
        Card card = new Card(new CardAsset("test", "Test Card", 1, 2, 3, "Minion"));
        duel.getDuelist(0).addToHand(card);

        CardSpatialComponent spatial = new CardSpatialComponent();
        assertTrue(spatial.needsResolution(card));

        CardSpatialResolutionSystem.resolvePosition(card, spatial, boardLayout);

        assertFalse(spatial.needsResolution(card));
        assertNotNull(spatial.getTargetPosition());
    }

    @Test
    void doesNotResolveWhenAlreadyResolved() {
        Card card = new Card(new CardAsset("test", "Test Card", 1, 2, 3, "Minion"));
        duel.getDuelist(0).addToHand(card);

        CardSpatialComponent spatial = new CardSpatialComponent();
        spatial.markResolved(card);

        CardSpatialResolutionSystem.resolvePosition(card, spatial, boardLayout);

        assertNull(spatial.getTargetPosition());
    }

    @Test
    void resolvesPositionForCardOnBattlefield() {
        Card card = new Card(new CardAsset("test", "Test Card", 1, 2, 3, "Minion"));
        duel.getDuelist(0).playCard(card);

        CardSpatialComponent spatial = new CardSpatialComponent();

        CardSpatialResolutionSystem.resolvePosition(card, spatial, boardLayout);

        assertFalse(spatial.needsResolution(card));
        assertNotNull(spatial.getTargetPosition());
    }

    @Test
    void resolvesWhenZoneChanges() {
        Card card = new Card(new CardAsset("test", "Test Card", 1, 2, 3, "Minion"));
        duel.getDuelist(0).addToHand(card);

        CardSpatialComponent spatial = new CardSpatialComponent();
        CardSpatialResolutionSystem.resolvePosition(card, spatial, boardLayout);

        assertFalse(spatial.needsResolution(card));

        // Move card to battlefield
        duel.getDuelist(0).playCard(card);

        assertTrue(spatial.needsResolution(card));

        CardSpatialResolutionSystem.resolvePosition(card, spatial, boardLayout);

        assertFalse(spatial.needsResolution(card));
    }

    @Test
    void resolvesWhenIndexChanges() {
        Card card1 = new Card(new CardAsset("test1", "Test Card 1", 1, 2, 3, "Minion"));
        Card card2 = new Card(new CardAsset("test2", "Test Card 2", 1, 2, 3, "Minion"));
        duel.getDuelist(0).addToHand(card1);
        duel.getDuelist(0).addToHand(card2);
        // addToHand places at index 0, so cards = [card2, card1]
        // card2 is at index 0, card1 is at index 1

        CardSpatialComponent spatial1 = new CardSpatialComponent();
        CardSpatialComponent spatial2 = new CardSpatialComponent();

        CardSpatialResolutionSystem.resolvePosition(card1, spatial1, boardLayout);
        CardSpatialResolutionSystem.resolvePosition(card2, spatial2, boardLayout);

        assertFalse(spatial1.needsResolution(card1));
        assertFalse(spatial2.needsResolution(card2));

        // Remove card2 (at index 0), shifting card1's index from 1 to 0
        duel.getDuelist(0).getHand().remove(card2);

        assertTrue(spatial1.needsResolution(card1));
    }
}