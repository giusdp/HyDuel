package com.giusdp.htduels.duel.handler.animation;

import com.giusdp.htduels.component.CardHoverComponent;
import com.giusdp.htduels.duel.event.CardUnhovered;
import com.giusdp.htduels.duel.event.DuelEvent;
import com.giusdp.htduels.duel.handler.DuelEventHandler;

public class CardUnhoveredHandler extends DuelEventHandler {

    @Override
    public void accept(DuelEvent ev) {
        CardUnhovered unhoveredEv = (CardUnhovered) ev;

        var hoverComponent = unhoveredEv.cardRef.getStore().getComponent(unhoveredEv.cardRef, CardHoverComponent.getComponentType());
        if (hoverComponent != null) {
            hoverComponent.setHovered(false);
        }
    }
}
