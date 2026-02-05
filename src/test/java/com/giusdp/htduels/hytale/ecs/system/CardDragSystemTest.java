package com.giusdp.htduels.hytale.ecs.system;

import com.hypixel.hytale.math.Vec2f;
import com.hypixel.hytale.math.vector.Vector3d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardDragSystemTest {

    @Test
    void applyDragUpdatesXZToMousePositionAndPreservesY() {
        Vector3d position = new Vector3d(1, 2, 3);
        Vec2f mousePos = new Vec2f(10, 20);

        CardDragSystem.applyDrag(position, mousePos);

        assertEquals(10, position.x, 0.001);
        assertEquals(2, position.y, 0.001);
        assertEquals(20, position.z, 0.001);
    }
}
