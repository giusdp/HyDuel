package com.giusdp.htduels.duel.handlers;

import com.giusdp.htduels.duel.event.DrawCards;
import com.giusdp.htduels.duel.event.DuelEvent;

public class DrawCardsLogHandler extends DuelEventHandler {
    @Override
    public void accept(DuelEvent event) {
        DrawCards drawCards = (DrawCards) event;
        System.out.println("[Duel] Player " + drawCards.duelist + " draws " + drawCards.count + " cards");
    }
}
