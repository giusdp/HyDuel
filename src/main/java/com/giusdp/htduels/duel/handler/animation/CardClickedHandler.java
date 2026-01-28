package com.giusdp.htduels.duel.handler.animation;

import com.giusdp.htduels.duel.event.CardClicked;
import com.giusdp.htduels.duel.event.DuelEvent;
import com.giusdp.htduels.duel.handler.DuelEventHandler;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;

public class CardClickedHandler extends DuelEventHandler {

    @Override
    public void accept(DuelEvent ev) {
        CardClicked hoveredEv = (CardClicked) ev;
        var transform = hoveredEv.cardRef.getStore().getComponent(hoveredEv.cardRef, TransformComponent.getComponentType());
        var pos = transform.getPosition();
        pos.y += 0.05;
    }

}