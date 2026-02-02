package com.giusdp.htduels.presentation.ecs.system;

import com.giusdp.htduels.presentation.ecs.component.CardHoverComponent;
import com.giusdp.htduels.presentation.ecs.component.CardSpatialComponent;
import com.hypixel.hytale.math.Vec2f;
import com.hypixel.hytale.math.vector.Vector3d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardHoverSystemTest {

    private CardSpatialComponent spatialAt(float y) {
        CardSpatialComponent spatial = new CardSpatialComponent();
        spatial.setTargetPosition(new Vec2f(0, 0));
        spatial.setTargetY(y);
        return spatial;
    }

    @Test
    void raisesCardWhenHovered() {
        float baseY = 2.0f;
        CardHoverComponent hover = new CardHoverComponent();
        CardSpatialComponent spatial = spatialAt(baseY);
        Vector3d position = new Vector3d(0, baseY, 0);

        hover.setHovered(true);
        CardHoverSystem.applyHover(hover, spatial, position);

        assertEquals(baseY + CardHoverSystem.HOVER_OFFSET, position.y, 0.001);
    }

    @Test
    void lowersCardWhenUnhovered() {
        float baseY = 2.0f;
        CardHoverComponent hover = new CardHoverComponent();
        CardSpatialComponent spatial = spatialAt(baseY);
        Vector3d position = new Vector3d(0, baseY + CardHoverSystem.HOVER_OFFSET, 0);

        hover.setHovered(false);
        CardHoverSystem.applyHover(hover, spatial, position);

        assertEquals(baseY, position.y, 0.001);
    }

    @Test
    void doesNotChangePositionWhenAlreadyAtTarget() {
        float baseY = 2.0f;
        CardHoverComponent hover = new CardHoverComponent();
        CardSpatialComponent spatial = spatialAt(baseY);
        Vector3d position = new Vector3d(0, baseY, 0);

        hover.setHovered(false);
        CardHoverSystem.applyHover(hover, spatial, position);

        assertEquals(baseY, position.y, 0.001);
    }

    @Test
    void hoverThenUnhoverReturnToOriginal() {
        float baseY = 2.0f;
        CardHoverComponent hover = new CardHoverComponent();
        CardSpatialComponent spatial = spatialAt(baseY);
        Vector3d position = new Vector3d(0, baseY, 0);

        hover.setHovered(true);
        CardHoverSystem.applyHover(hover, spatial, position);
        assertEquals(baseY + CardHoverSystem.HOVER_OFFSET, position.y, 0.001);

        hover.setHovered(false);
        CardHoverSystem.applyHover(hover, spatial, position);
        assertEquals(baseY, position.y, 0.001);
    }
}
