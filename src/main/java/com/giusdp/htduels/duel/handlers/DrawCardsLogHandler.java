package com.giusdp.htduels.duel.handlers;

import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.event.DrawCards;
import com.giusdp.htduels.duel.event.DuelEvent;
import org.jspecify.annotations.NonNull;

public class DrawCardsLogHandler extends DuelEventHandler {
    public DrawCardsLogHandler(Duel duel) {
        super(duel);
    }

    @Override
    public void accept(DuelEvent event) {
        DrawCards drawCards = (DrawCards) event;
        System.out.println("[Duel] Player " + drawCards.duelist() + " draws " + drawCards.count() + " cards");
    }
}
