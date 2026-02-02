package com.giusdp.htduels.duel.positioning;

import com.giusdp.htduels.FakeCardRepo;

import com.giusdp.htduels.TestBoardLayout;
import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.duel.Card;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duelist.BotTurnStrategy;
import com.giusdp.htduels.duelist.PlayerTurnStrategy;
import com.giusdp.htduels.duelist.Duelist;
import com.hypixel.hytale.math.Vec2f;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.Rotation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardLayoutsTest {
    Duel duel;
    BoardLayout boardLayout;

    @BeforeEach
    void setup() {
        boardLayout = TestBoardLayout.create();
        duel = Duel.builder()
                .cardRepo(new FakeCardRepo())
                .addDuelist(new Duelist(new PlayerTurnStrategy()), true)
                .addDuelist(new Duelist(new BotTurnStrategy()), false)
                .build();
    }

    @Test
    void singleCardInHandIsCenteredAtOrigin() {
        Card card = new Card(new CardAsset("test", "Test Card", 1, 2, 3, "Minion"));
        duel.getDuelist(0).addToHand(card);

        Vec2f position = CardLayouts.hand(card, boardLayout);

        // With origin at (0,0) and playerHandDepth=4, position should be at (0, -4)
        assertEquals(0, position.x, 0.001f);
        assertEquals(-4, position.y, 0.001f);
    }

    @Test
    void multipleCardsInHandAreEvenlySpacedAroundCenter() {
        Card card1 = new Card(new CardAsset("test1", "Test Card 1", 1, 2, 3, "Minion"));
        Card card2 = new Card(new CardAsset("test2", "Test Card 2", 1, 2, 3, "Minion"));
        Card card3 = new Card(new CardAsset("test3", "Test Card 3", 1, 2, 3, "Minion"));
        duel.getDuelist(0).addToHand(card1);
        duel.getDuelist(0).addToHand(card2);
        duel.getDuelist(0).addToHand(card3);

        Vec2f pos1 = CardLayouts.hand(card1, boardLayout);
        Vec2f pos2 = CardLayouts.hand(card2, boardLayout);
        Vec2f pos3 = CardLayouts.hand(card3, boardLayout);

        float spacing = boardLayout.handSpacing();

        // Cards are evenly spaced along X axis
        assertEquals(spacing, Math.abs(pos2.x - pos1.x), 0.001f);
        assertEquals(spacing, Math.abs(pos3.x - pos2.x), 0.001f);

        // Middle card is at center X
        assertEquals(0, pos2.x, 0.001f);

        // All cards share same Y position (depth)
        assertEquals(-4, pos1.y, 0.001f);
        assertEquals(-4, pos2.y, 0.001f);
        assertEquals(-4, pos3.y, 0.001f);
    }

    @Test
    void bottomPlayerUsesNegativeDepth() {
        Card card = new Card(new CardAsset("test", "Test Card", 1, 2, 3, "Minion"));
        duel.getDuelist(0).addToHand(card);

        Vec2f position = CardLayouts.hand(card, boardLayout);

        assertEquals(-4, position.y, 0.001f);
    }

    @Test
    void topPlayerUsesPositiveDepth() {
        Card card = new Card(new CardAsset("test", "Test Card", 1, 2, 3, "Minion"));
        duel.getDuelist(1).addToHand(card);

        Vec2f position = CardLayouts.hand(card, boardLayout);

        assertEquals(4, position.y, 0.001f);
    }

    @Test
    void twoCardsInHandAreSymmetricallySpacedAroundCenter() {
        Card card1 = new Card(new CardAsset("test1", "Test Card 1", 1, 2, 3, "Minion"));
        Card card2 = new Card(new CardAsset("test2", "Test Card 2", 1, 2, 3, "Minion"));
        duel.getDuelist(0).addToHand(card1);
        duel.getDuelist(0).addToHand(card2);

        Vec2f pos1 = CardLayouts.hand(card1, boardLayout);
        Vec2f pos2 = CardLayouts.hand(card2, boardLayout);

        float spacing = boardLayout.handSpacing();

        // Two cards should be spacing apart
        assertEquals(spacing, Math.abs(pos2.x - pos1.x), 0.001f);

        // Cards should be symmetrically placed around center (x=0)
        assertEquals(0, (pos1.x + pos2.x) / 2, 0.001f);
    }

    @Nested
    class RotationTests {

        @Test
        void rotationNonePlacesPlayerHandAtNegativeZ() {
            boardLayout = TestBoardLayout.create(Rotation.None);
            Card card = new Card(new CardAsset("test", "Test Card", 1, 2, 3, "Minion"));
            duel.getDuelist(0).addToHand(card);

            Vec2f pos = CardLayouts.hand(card, boardLayout);

            // No rotation: local (0, -4) stays at world (0, -4)
            assertEquals(0, pos.x, 0.001f);
            assertEquals(-4, pos.y, 0.001f);
        }

        @Test
        void rotationNinetyRotatesPositions90DegreesClockwise() {
            boardLayout = TestBoardLayout.create(Rotation.Ninety);
            Card card = new Card(new CardAsset("test", "Test Card", 1, 2, 3, "Minion"));
            duel.getDuelist(0).addToHand(card);

            Vec2f pos = CardLayouts.hand(card, boardLayout);

            // Ninety.rotateY: (x,y,z) -> (z, y, -x) so (0, 0, -4) -> (-4, 0, 0)
            assertEquals(-4, pos.x, 0.001f);
            assertEquals(0, pos.y, 0.001f);
        }

        @Test
        void rotationOneEightyRotatesPositions180Degrees() {
            boardLayout = TestBoardLayout.create(Rotation.OneEighty);
            Card card = new Card(new CardAsset("test", "Test Card", 1, 2, 3, "Minion"));
            duel.getDuelist(0).addToHand(card);

            Vec2f pos = CardLayouts.hand(card, boardLayout);

            // 180° rotation: local (0, -4) becomes world (0, 4)
            // rotateY for 180°: (x,y,z) -> (-x, y, -z)
            // So (0, 0, -4) -> (0, 0, 4)
            assertEquals(0, pos.x, 0.001f);
            assertEquals(4, pos.y, 0.001f);
        }

        @Test
        void rotationTwoSeventyRotatesPositions270Degrees() {
            boardLayout = TestBoardLayout.create(Rotation.TwoSeventy);
            Card card = new Card(new CardAsset("test", "Test Card", 1, 2, 3, "Minion"));
            duel.getDuelist(0).addToHand(card);

            Vec2f pos = CardLayouts.hand(card, boardLayout);

            // TwoSeventy.rotateY: (x,y,z) -> (-z, y, x) so (0, 0, -4) -> (4, 0, 0)
            assertEquals(4, pos.x, 0.001f);
            assertEquals(0, pos.y, 0.001f);
        }

        @Test
        void multipleCardsRotateCorrectlyWithNinetyDegreeRotation() {
            boardLayout = TestBoardLayout.create(Rotation.Ninety);
            Card card1 = new Card(new CardAsset("test1", "Test Card 1", 1, 2, 3, "Minion"));
            Card card2 = new Card(new CardAsset("test2", "Test Card 2", 1, 2, 3, "Minion"));
            Card card3 = new Card(new CardAsset("test3", "Test Card 3", 1, 2, 3, "Minion"));
            duel.getDuelist(0).addToHand(card1);
            duel.getDuelist(0).addToHand(card2);
            duel.getDuelist(0).addToHand(card3);

            Vec2f pos1 = CardLayouts.hand(card1, boardLayout);
            Vec2f pos2 = CardLayouts.hand(card2, boardLayout);
            Vec2f pos3 = CardLayouts.hand(card3, boardLayout);

            // Ninety.rotateY: (x,y,z) -> (z, y, -x)
            // Cards spread along Y axis (world Z) instead of X
            // Middle card at center
            assertEquals(-4, pos2.x, 0.001f);
            assertEquals(0, pos2.y, 0.001f);

            // Cards spaced along Y axis (which is world Z)
            float spacing = boardLayout.handSpacing();
            assertEquals(spacing, Math.abs(pos2.y - pos1.y), 0.001f);
            assertEquals(spacing, Math.abs(pos3.y - pos2.y), 0.001f);

            // All cards same X position
            assertEquals(-4, pos1.x, 0.001f);
            assertEquals(-4, pos3.x, 0.001f);
        }
    }
}
