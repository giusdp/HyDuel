package com.giusdp.htduels.system;

import com.hypixel.hytale.math.Vec2f;
import com.hypixel.hytale.math.vector.Vector3d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardMovementSystemTest {

    @Test
    void applyMovementSetsXZToTargetAndPreservesY() {
        Vector3d position = new Vector3d(1, 2, 3);
        Vec2f target = new Vec2f(5, 10);

        CardMovementSystem.applyMovement(position, target);

        assertEquals(5, position.x, 0.001);
        assertEquals(2, position.y, 0.001);
        assertEquals(10, position.z, 0.001);
    }
}
