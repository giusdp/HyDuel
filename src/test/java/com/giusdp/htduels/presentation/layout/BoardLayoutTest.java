package com.giusdp.htduels.presentation.layout;

import com.giusdp.htduels.TestBoardLayout;
import com.hypixel.hytale.math.Vec2f;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.Rotation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardLayoutTest {

    @Nested
    class ToLocalPositionTests {

        @Test
        void roundTripsWithNoRotation() {
            BoardLayout layout = TestBoardLayout.create(Rotation.None);
            Vec2f world = layout.toWorldPosition(3f, -2f);
            Vec2f local = layout.toLocalPosition(world);

            assertEquals(3f, local.x, 0.001f);
            assertEquals(-2f, local.y, 0.001f);
        }

        @Test
        void roundTripsWithNinetyRotation() {
            BoardLayout layout = TestBoardLayout.create(Rotation.Ninety);
            Vec2f world = layout.toWorldPosition(3f, -2f);
            Vec2f local = layout.toLocalPosition(world);

            assertEquals(3f, local.x, 0.001f);
            assertEquals(-2f, local.y, 0.001f);
        }

        @Test
        void roundTripsWithOneEightyRotation() {
            BoardLayout layout = TestBoardLayout.create(Rotation.OneEighty);
            Vec2f world = layout.toWorldPosition(3f, -2f);
            Vec2f local = layout.toLocalPosition(world);

            assertEquals(3f, local.x, 0.001f);
            assertEquals(-2f, local.y, 0.001f);
        }

        @Test
        void roundTripsWithTwoSeventyRotation() {
            BoardLayout layout = TestBoardLayout.create(Rotation.TwoSeventy);
            Vec2f world = layout.toWorldPosition(3f, -2f);
            Vec2f local = layout.toLocalPosition(world);

            assertEquals(3f, local.x, 0.001f);
            assertEquals(-2f, local.y, 0.001f);
        }

        @Test
        void originMapsToZero() {
            BoardLayout layout = TestBoardLayout.create();
            Vec2f local = layout.toLocalPosition(layout.boardOrigin());

            assertEquals(0f, local.x, 0.001f);
            assertEquals(0f, local.y, 0.001f);
        }
    }

    @Nested
    class IsInBattlefieldZoneTests {

        @Test
        void positionAtBattlefieldDepthIsInZone() {
            BoardLayout layout = TestBoardLayout.create();
            // Bottom player battlefield is at localZ = -playerBattlefieldDepth = -2
            Vec2f worldPos = layout.toWorldPosition(0f, -layout.playerBattlefieldDepth());

            assertTrue(layout.isInBattlefieldZone(worldPos, true));
        }

        @Test
        void positionAtHandDepthIsInZone() {
            BoardLayout layout = TestBoardLayout.create();
            // Hand depth = -4, still within the zone range (0 to -4)
            Vec2f worldPos = layout.toWorldPosition(0f, -layout.playerHandDepth());

            assertTrue(layout.isInBattlefieldZone(worldPos, true));
        }

        @Test
        void positionAtOriginIsInZone() {
            BoardLayout layout = TestBoardLayout.create();
            Vec2f worldPos = layout.toWorldPosition(0f, 0f);

            assertTrue(layout.isInBattlefieldZone(worldPos, true));
        }

        @Test
        void positionBeyondHandDepthIsNotInZone() {
            BoardLayout layout = TestBoardLayout.create();
            // Beyond hand depth on player side
            Vec2f worldPos = layout.toWorldPosition(0f, -(layout.playerHandDepth() + 1f));

            assertFalse(layout.isInBattlefieldZone(worldPos, true));
        }

        @Test
        void positionOnOpponentSideIsNotInZoneForOpponentPlayer() {
            BoardLayout layout = TestBoardLayout.create();
            // Opponent side (positive Z for opponent player)
            Vec2f worldPos = layout.toWorldPosition(0f, layout.opponentBattlefieldDepth());

            assertFalse(layout.isInBattlefieldZone(worldPos, true));
        }

        @Test
        void opponentPositionAtOpponentDepthIsInZone() {
            BoardLayout layout = TestBoardLayout.create();
            // Top player (not bottom) battlefield is at positive depth
            Vec2f worldPos = layout.toWorldPosition(0f, layout.opponentBattlefieldDepth());

            assertTrue(layout.isInBattlefieldZone(worldPos, false));
        }

        @Test
        void opponentPositionOnPlayerSideIsNotInZone() {
            BoardLayout layout = TestBoardLayout.create();
            Vec2f worldPos = layout.toWorldPosition(0f, -layout.playerBattlefieldDepth());

            assertFalse(layout.isInBattlefieldZone(worldPos, false));
        }

        @Test
        void worksWithNinetyRotation() {
            BoardLayout layout = TestBoardLayout.create(Rotation.Ninety);
            Vec2f worldPos = layout.toWorldPosition(0f, -layout.playerBattlefieldDepth());

            assertTrue(layout.isInBattlefieldZone(worldPos, true));
        }

        @Test
        void worksWithOneEightyRotation() {
            BoardLayout layout = TestBoardLayout.create(Rotation.OneEighty);
            Vec2f worldPos = layout.toWorldPosition(0f, -layout.playerBattlefieldDepth());

            assertTrue(layout.isInBattlefieldZone(worldPos, true));
        }
    }
}
