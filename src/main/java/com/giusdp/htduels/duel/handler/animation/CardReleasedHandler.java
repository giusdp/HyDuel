package com.giusdp.htduels.duel.handler.animation;

import com.giusdp.htduels.duel.event.CardClicked;
import com.giusdp.htduels.duel.event.DuelEvent;
import com.giusdp.htduels.duel.handler.DuelEventHandler;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;

public class CardReleasedHandler extends DuelEventHandler {

    @Override
    public void accept(DuelEvent ev) {
        System.out.println("Card Released Handler triggered");
    }

}