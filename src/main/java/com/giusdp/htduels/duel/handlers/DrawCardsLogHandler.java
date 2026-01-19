package com.giusdp.htduels.duel.handlers;

import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.MoveHandler;
import com.giusdp.htduels.duel.moves.DrawCards;
import com.giusdp.htduels.duel.moves.Move;
import org.jetbrains.annotations.UnknownNullability;

public class DrawCardsLogHandler implements MoveHandler {
    @Override
    public void handle(Move move, Duel duel) {
        DrawCards drawCards = (DrawCards) move;
        System.out.println("[Duel] Player " + drawCards.playerIndex() + " draws " + drawCards.count() + " cards");
    }
}
