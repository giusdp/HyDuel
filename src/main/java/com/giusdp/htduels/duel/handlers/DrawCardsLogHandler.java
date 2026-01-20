package com.giusdp.htduels.duel.handlers;

import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.MoveHandler;
import com.giusdp.htduels.duel.event.DrawCards;
import com.giusdp.htduels.duel.event.DuelEvent;

public class DrawCardsLogHandler implements MoveHandler {
    @Override
    public void handle(DuelEvent move, Duel duel) {
        DrawCards drawCards = (DrawCards) move;
        System.out.println("[Duel] Player " + drawCards.playerIndex() + " draws " + drawCards.count() + " cards");
    }
}
