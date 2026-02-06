package com.giusdp.htduels.hytale.ecs.system;

import com.giusdp.htduels.FakeCardRepo;

import com.giusdp.htduels.TestBoardLayout;
import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.hytale.ecs.component.CardComponent;
import com.giusdp.htduels.hytale.ecs.component.CardSpatialComponent;
import com.giusdp.htduels.match.Card;
import com.giusdp.htduels.match.Duel;
import com.giusdp.htduels.hytale.layout.BoardLayout;
import com.giusdp.htduels.match.BotTurnStrategy;
import com.giusdp.htduels.match.HumanTurnStrategy;
import com.giusdp.htduels.match.Duelist;
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
                .addDuelist(new Duelist(new HumanTurnStrategy()), true)
                .addDuelist(new Duelist(new BotTurnStrategy()), false)
                .build();
    }

    private CardComponent toCardComponent(Card card) {
        return new CardComponent(
                card.getId(), null,
                card.getCurrentZoneType(),
                card.getZoneIndex(),
                card.getZone().getCards().size(),
                card.getOwner().isOpponentSide()
        );
    }

    @Test
    void resolvesPositionWhenDirty() {
        Card card = new Card(new CardAsset("test", "Test Card", 1, 2, 3, "Minion"));
        duel.getDuelist(0).addToHand(card);
        CardComponent cc = toCardComponent(card);

        CardSpatialComponent spatial = new CardSpatialComponent();
        assertTrue(spatial.needsResolution(cc));

        CardSpatialResolutionSystem.resolvePosition(cc, spatial, boardLayout);

        assertFalse(spatial.needsResolution(cc));
        assertNotNull(spatial.getTargetPosition());
    }

    @Test
    void doesNotResolveWhenAlreadyResolved() {
        Card card = new Card(new CardAsset("test", "Test Card", 1, 2, 3, "Minion"));
        duel.getDuelist(0).addToHand(card);
        CardComponent cc = toCardComponent(card);

        CardSpatialComponent spatial = new CardSpatialComponent();
        spatial.markResolved(cc);

        CardSpatialResolutionSystem.resolvePosition(cc, spatial, boardLayout);

        assertNull(spatial.getTargetPosition());
    }

    @Test
    void resolvesPositionForCardOnBattlefield() {
        Card card = new Card(new CardAsset("test", "Test Card", 1, 2, 3, "Minion"));
        duel.getDuelist(0).playCard(card);
        CardComponent cc = toCardComponent(card);

        CardSpatialComponent spatial = new CardSpatialComponent();

        CardSpatialResolutionSystem.resolvePosition(cc, spatial, boardLayout);

        assertFalse(spatial.needsResolution(cc));
        assertNotNull(spatial.getTargetPosition());
    }

    @Test
    void resolvesWhenZoneChanges() {
        Card card = new Card(new CardAsset("test", "Test Card", 1, 2, 3, "Minion"));
        duel.getDuelist(0).addToHand(card);
        CardComponent cc = toCardComponent(card);

        CardSpatialComponent spatial = new CardSpatialComponent();
        CardSpatialResolutionSystem.resolvePosition(cc, spatial, boardLayout);

        assertFalse(spatial.needsResolution(cc));

        // Move card to battlefield
        duel.getDuelist(0).playCard(card);
        // Update the component cache to reflect the move
        cc.setZoneType(card.getCurrentZoneType());
        cc.setZoneIndex(card.getZoneIndex());
        cc.setZoneSize(card.getZone().getCards().size());

        assertTrue(spatial.needsResolution(cc));

        CardSpatialResolutionSystem.resolvePosition(cc, spatial, boardLayout);

        assertFalse(spatial.needsResolution(cc));
    }

    @Test
    void resolvesWhenIndexChanges() {
        Card card1 = new Card(new CardAsset("test1", "Test Card 1", 1, 2, 3, "Minion"));
        Card card2 = new Card(new CardAsset("test2", "Test Card 2", 1, 2, 3, "Minion"));
        duel.getDuelist(0).addToHand(card1);
        duel.getDuelist(0).addToHand(card2);

        CardComponent cc1 = toCardComponent(card1);

        CardSpatialComponent spatial1 = new CardSpatialComponent();

        CardSpatialResolutionSystem.resolvePosition(cc1, spatial1, boardLayout);

        assertFalse(spatial1.needsResolution(cc1));

        // Remove card2 (at index 0), shifting card1's index from 1 to 0
        duel.getDuelist(0).getHand().remove(card2);

        // Update card component cache
        cc1.setZoneIndex(card1.getZoneIndex());
        cc1.setZoneSize(card1.getZone().getCards().size());

        assertTrue(spatial1.needsResolution(cc1));
    }
}
