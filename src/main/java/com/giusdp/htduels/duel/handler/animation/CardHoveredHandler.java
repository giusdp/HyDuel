package com.giusdp.htduels.duel.handler.animation;

import com.giusdp.htduels.duel.event.CardHovered;
import com.giusdp.htduels.duel.event.DuelEvent;
import com.giusdp.htduels.duel.handler.DuelEventHandler;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;

public class CardHoveredHandler extends DuelEventHandler {

    @Override
    public void accept(DuelEvent ev) {
        CardHovered hoveredEv = (CardHovered) ev;
        hoveredEv.cardRef.getStore().getComponent(hoveredEv.cardRef, TransformComponent.getComponentType());

        var transform = hoveredEv.cardRef.getStore().getComponent(hoveredEv.cardRef, TransformComponent.getComponentType());
        var pos = transform.getPosition();
        pos.y += 0.05;

    }

}