package com.giusdp.htduels.interaction;

import com.giusdp.htduels.DuelSession;
import com.hypixel.hytale.math.Vec2f;
import com.hypixel.hytale.protocol.Position;
import com.hypixel.hytale.protocol.Vector2f;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardInteractionServiceTest {

    private DuelSession.DuelSpatialData spatialData(Position cameraPos, float cameraYaw, float cardY) {
        return new DuelSession.DuelSpatialData(cameraPos, cameraYaw, cardY);
    }

    @Nested
    class ScreenToWorldTests {

        @Test
        void centerScreenMapsToCameraPosition() {
            // screenPoint (0, 0) should map to camera XZ position
            var spatial = spatialData(new Position(10, 5, 20), 0f, 3f);
            Vec2f result = CardInteractionService.screenToWorld(new Vector2f(0, 0), spatial);

            assertEquals(10f, result.x, 0.001f);
            assertEquals(20f, result.y, 0.001f);
        }

        @Test
        void rightEdgeScreenMapsToPositiveX_NoRotation() {
            // screenPoint (1, 0) with no rotation should be +X from camera
            float cardY = 3f;
            var spatial = spatialData(new Position(0, 5, 0), 0f, cardY);

            // Camera height = 5 - 3 = 2
            // FOV = 80 degrees, halfFov = 40 degrees
            // halfHeight = 2 * tan(40°) ≈ 2 * 0.839 ≈ 1.678
            // halfWidth = halfHeight * (16/9) ≈ 1.678 * 1.778 ≈ 2.98
            float cameraHeight = 2f;
            float halfHeight = cameraHeight * (float) Math.tan(Math.toRadians(40));
            float expectedHalfWidth = halfHeight * (16f / 9f);

            Vec2f result = CardInteractionService.screenToWorld(new Vector2f(1, 0), spatial);

            assertEquals(expectedHalfWidth, result.x, 0.01f);
            assertEquals(0f, result.y, 0.001f);
        }

        @Test
        void leftEdgeScreenMapsToNegativeX_NoRotation() {
            float cardY = 3f;
            var spatial = spatialData(new Position(0, 5, 0), 0f, cardY);

            float cameraHeight = 2f;
            float halfHeight = cameraHeight * (float) Math.tan(Math.toRadians(40));
            float expectedHalfWidth = halfHeight * (16f / 9f);

            Vec2f result = CardInteractionService.screenToWorld(new Vector2f(-1, 0), spatial);

            assertEquals(-expectedHalfWidth, result.x, 0.01f);
            assertEquals(0f, result.y, 0.001f);
        }

        @Test
        void topEdgeScreenMapsToNegativeZ_NoRotation() {
            // screenPoint (0, 1) with no rotation should be -Z from camera
            // (top of screen = forward = -Z in standard coords)
            float cardY = 3f;
            var spatial = spatialData(new Position(0, 5, 0), 0f, cardY);

            float cameraHeight = 2f;
            float expectedHalfHeight = cameraHeight * (float) Math.tan(Math.toRadians(40));

            Vec2f result = CardInteractionService.screenToWorld(new Vector2f(0, 1), spatial);

            assertEquals(0f, result.x, 0.001f);
            // With no rotation, screen Y maps directly to world Z
            assertEquals(expectedHalfHeight, result.y, 0.01f);
        }

        @Test
        void bottomEdgeScreenMapsToPositiveZ_NoRotation() {
            float cardY = 3f;
            var spatial = spatialData(new Position(0, 5, 0), 0f, cardY);

            float cameraHeight = 2f;
            float expectedHalfHeight = cameraHeight * (float) Math.tan(Math.toRadians(40));

            Vec2f result = CardInteractionService.screenToWorld(new Vector2f(0, -1), spatial);

            assertEquals(0f, result.x, 0.001f);
            assertEquals(-expectedHalfHeight, result.y, 0.01f);
        }

        @Test
        void rotation90DegreesSwapsAxes() {
            // With 90° yaw, X becomes Z and Z becomes -X
            float yaw = (float) Math.PI / 2;  // 90 degrees
            float cardY = 3f;
            var spatial = spatialData(new Position(0, 5, 0), yaw, cardY);

            float cameraHeight = 2f;
            float halfHeight = cameraHeight * (float) Math.tan(Math.toRadians(40));
            float halfWidth = halfHeight * (16f / 9f);

            Vec2f result = CardInteractionService.screenToWorld(new Vector2f(1, 0), spatial);

            // Right edge should now be +Z instead of +X
            assertEquals(0f, result.x, 0.1f);
            assertEquals(halfWidth, result.y, 0.1f);
        }

        @Test
        void rotation180DegreesInvertsAxes() {
            // With 180° yaw, X becomes -X and Z becomes -Z
            float yaw = (float) Math.PI;  // 180 degrees
            float cardY = 3f;
            var spatial = spatialData(new Position(0, 5, 0), yaw, cardY);

            float cameraHeight = 2f;
            float halfHeight = cameraHeight * (float) Math.tan(Math.toRadians(40));
            float halfWidth = halfHeight * (16f / 9f);

            Vec2f result = CardInteractionService.screenToWorld(new Vector2f(1, 0), spatial);

            // Right edge should now be -X
            assertEquals(-halfWidth, result.x, 0.01f);
            assertEquals(0f, result.y, 0.1f);
        }

        @Test
        void cameraPositionOffsetsResult() {
            var spatial = spatialData(new Position(100, 10, 200), 0f, 5f);
            Vec2f result = CardInteractionService.screenToWorld(new Vector2f(0, 0), spatial);

            assertEquals(100f, result.x, 0.001f);
            assertEquals(200f, result.y, 0.001f);
        }

        @Test
        void higherCameraGivesWiderView() {
            float cardY = 3f;
            var lowSpatial = spatialData(new Position(0, 5, 0), 0f, cardY);
            var highSpatial = spatialData(new Position(0, 10, 0), 0f, cardY);

            Vec2f lowResult = CardInteractionService.screenToWorld(new Vector2f(1, 0), lowSpatial);
            Vec2f highResult = CardInteractionService.screenToWorld(new Vector2f(1, 0), highSpatial);

            // Higher camera should map to a point further from center
            assertTrue(highResult.x > lowResult.x);
        }
    }
}
