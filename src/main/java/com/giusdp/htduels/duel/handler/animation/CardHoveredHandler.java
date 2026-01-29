package com.giusdp.htduels.duel.handler.animation;

import com.giusdp.htduels.component.CardHoverComponent;
import com.giusdp.htduels.duel.event.CardHovered;
import com.giusdp.htduels.duel.event.DuelEvent;
import com.giusdp.htduels.duel.handler.DuelEventHandler;

public class CardHoveredHandler extends DuelEventHandler {

    @Override
    public void accept(DuelEvent ev) {
        CardHovered hoveredEv = (CardHovered) ev;

        var hoverComponent = hoveredEv.cardRef.getStore().getComponent(hoveredEv.cardRef, CardHoverComponent.getComponentType());
        if (hoverComponent != null) {
            hoverComponent.setHovered(true);
        }
    }
}
