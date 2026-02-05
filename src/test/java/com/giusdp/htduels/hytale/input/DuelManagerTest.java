package com.giusdp.htduels.hytale.input;

import com.giusdp.htduels.hytale.camera.BoardCameraService;
import com.giusdp.htduels.hytale.DuelManager;
import com.giusdp.htduels.hytale.layout.BoardLayout;
import com.hypixel.hytale.math.Vec2f;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.Position;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.Rotation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DuelManagerTest {

    @Nested
    class CreateBoardLayoutTests {

        @Test
        void noneRotationOffsetsZByHalf() {
            BoardLayout layout = DuelManager.createBoardLayout(new Vector3i(10, 5, 20), Rotation.None);
            Vec2f origin = layout.boardOrigin();
            assertEquals(10f, origin.x, 0.001f);
            assertEquals(20.5f, origin.y, 0.001f);
        }

        @Test
        void ninetyRotationOffsetsXByHalfAndZByOne() {
            BoardLayout layout = DuelManager.createBoardLayout(new Vector3i(10, 5, 20), Rotation.Ninety);
            Vec2f origin = layout.boardOrigin();
            assertEquals(10.5f, origin.x, 0.001f);
            assertEquals(21f, origin.y, 0.001f);
        }

        @Test
        void oneEightyRotationOffsetsXByOneAndZByHalf() {
            BoardLayout layout = DuelManager.createBoardLayout(new Vector3i(10, 5, 20), Rotation.OneEighty);
            Vec2f origin = layout.boardOrigin();
            assertEquals(11f, origin.x, 0.001f);
            assertEquals(20.5f, origin.y, 0.001f);
        }

        @Test
        void twoSeventyRotationOffsetsXByHalf() {
            BoardLayout layout = DuelManager.createBoardLayout(new Vector3i(10, 5, 20), Rotation.TwoSeventy);
            Vec2f origin = layout.boardOrigin();
            assertEquals(10.5f, origin.x, 0.001f);
            assertEquals(20f, origin.y, 0.001f);
        }

        @Test
        void rotationIsPreserved() {
            BoardLayout layout = DuelManager.createBoardLayout(new Vector3i(0, 0, 0), Rotation.Ninety);
            assertEquals(Rotation.Ninety, layout.rotation());
        }
    }

    @Nested
    class CalculateCameraPositionTests {

        @Test
        void noneRotationCentersOnZ() {
            Position pos = BoardCameraService.calculateCameraPosition(new Vector3i(10, 5, 20), Rotation.None);
            assertEquals(10.0, pos.x, 0.001);
            assertEquals(5 + 1.75, pos.y, 0.001);
            assertEquals(20.5, pos.z, 0.001);
        }

        @Test
        void ninetyRotationCentersOnXAndZ() {
            Position pos = BoardCameraService.calculateCameraPosition(new Vector3i(10, 5, 20), Rotation.Ninety);
            assertEquals(10.5, pos.x, 0.001);
            assertEquals(5 + 1.75, pos.y, 0.001);
            assertEquals(21.0, pos.z, 0.001);
        }

        @Test
        void oneEightyRotationCentersOnXAndZ() {
            Position pos = BoardCameraService.calculateCameraPosition(new Vector3i(10, 5, 20), Rotation.OneEighty);
            assertEquals(11.0, pos.x, 0.001);
            assertEquals(5 + 1.75, pos.y, 0.001);
            assertEquals(20.5, pos.z, 0.001);
        }

        @Test
        void twoSeventyRotationCentersOnX() {
            Position pos = BoardCameraService.calculateCameraPosition(new Vector3i(10, 5, 20), Rotation.TwoSeventy);
            assertEquals(10.5, pos.x, 0.001);
            assertEquals(5 + 1.75, pos.y, 0.001);
            assertEquals(20.0, pos.z, 0.001);
        }

        @Test
        void yOffsetIsConsistent() {
            for (Rotation r : Rotation.values()) {
                Position pos = BoardCameraService.calculateCameraPosition(new Vector3i(0, 10, 0), r);
                assertEquals(11.75, pos.y, 0.001, "Y offset wrong for " + r);
            }
        }
    }
}
