package com.giusdp.htduels.presentation.ecs.system;

import com.giusdp.htduels.presentation.ecs.component.CardSpatialComponent;
import com.hypixel.hytale.math.Vec2f;
import com.hypixel.hytale.math.vector.Vector3d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardMovementSystemTest {

    @Test
    void applyMovementSetsXYZToTarget() {
        Vector3d position = new Vector3d(1, 2, 3);
        CardSpatialComponent spatial = new CardSpatialComponent();
        spatial.setTargetPosition(new Vec2f(5, 10));
        spatial.setTargetY(7);

        CardMovementSystem.applyMovement(position, spatial);

        assertEquals(5, position.x, 0.001);
        assertEquals(7, position.y, 0.001);
        assertEquals(10, position.z, 0.001);
    }
}
