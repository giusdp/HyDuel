package com.giusdp.htduels.system;

import com.giusdp.htduels.component.CardHoverComponent;
import com.hypixel.hytale.math.vector.Vector3d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardHoverSystemTest {

    @Test
    void raisesCardWhenHovered() {
        float originalY = 2.0f;
        CardHoverComponent hover = new CardHoverComponent(originalY);
        Vector3d position = new Vector3d(0, originalY, 0);

        hover.setHovered(true);
        CardHoverSystem.applyHover(hover, position);

        assertEquals(originalY + CardHoverSystem.HOVER_OFFSET, position.y, 0.001);
    }

    @Test
    void lowersCardWhenUnhovered() {
        float originalY = 2.0f;
        CardHoverComponent hover = new CardHoverComponent(originalY);
        Vector3d position = new Vector3d(0, originalY + CardHoverSystem.HOVER_OFFSET, 0);

        hover.setHovered(false);
        CardHoverSystem.applyHover(hover, position);

        assertEquals(originalY, position.y, 0.001);
    }

    @Test
    void doesNotChangePositionWhenAlreadyAtTarget() {
        float originalY = 2.0f;
        CardHoverComponent hover = new CardHoverComponent(originalY);
        Vector3d position = new Vector3d(0, originalY, 0);

        hover.setHovered(false);
        CardHoverSystem.applyHover(hover, position);

        assertEquals(originalY, position.y, 0.001);
    }

    @Test
    void hoverThenUnhoverReturnToOriginal() {
        float originalY = 2.0f;
        CardHoverComponent hover = new CardHoverComponent(originalY);
        Vector3d position = new Vector3d(0, originalY, 0);

        hover.setHovered(true);
        CardHoverSystem.applyHover(hover, position);
        assertEquals(originalY + CardHoverSystem.HOVER_OFFSET, position.y, 0.001);

        hover.setHovered(false);
        CardHoverSystem.applyHover(hover, position);
        assertEquals(originalY, position.y, 0.001);
    }
}
