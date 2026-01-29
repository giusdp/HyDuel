package com.giusdp.htduels;

import com.hypixel.hytale.protocol.Position;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerDuelContextTest {

    @Nested
    class DuelSpatialDataTests {

        @Test
        void storesCameraPosition() {
            Position cameraPos = new Position(10, 5, 20);
            var spatial = new PlayerDuelContext.DuelSpatialData(cameraPos, 1.5f, 3f);

            assertEquals(cameraPos, spatial.cameraPos());
        }

        @Test
        void storesCameraYaw() {
            Position cameraPos = new Position(0, 5, 0);
            float expectedYaw = 1.5f;
            var spatial = new PlayerDuelContext.DuelSpatialData(cameraPos, expectedYaw, 3f);

            assertEquals(expectedYaw, spatial.cameraYaw(), 0.001f);
        }

        @Test
        void storesCardY() {
            Position cameraPos = new Position(0, 5, 0);
            float expectedCardY = 3.5f;
            var spatial = new PlayerDuelContext.DuelSpatialData(cameraPos, 0f, expectedCardY);

            assertEquals(expectedCardY, spatial.cardY(), 0.001f);
        }

        @Test
        void cameraHeightCalculation() {
            // Camera at y=10, card at y=3 => height difference = 7
            Position cameraPos = new Position(0, 10, 0);
            var spatial = new PlayerDuelContext.DuelSpatialData(cameraPos, 0f, 3f);

            float cameraHeight = (float) (spatial.cameraPos().y - spatial.cardY());
            assertEquals(7f, cameraHeight, 0.001f);
        }
    }
}
