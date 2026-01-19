package com.giusdp.htduels.duel.handlers;

import com.giusdp.htduels.duel.Card;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.MoveHandler;
import com.giusdp.htduels.duel.moves.DrawCards;
import com.giusdp.htduels.duel.moves.Move;
import org.jetbrains.annotations.UnknownNullability;

public class DrawCardsHandler implements MoveHandler {
    @Override
    public void handle(Move move, Duel duel) {
        DrawCards drawCards = (DrawCards) move;
        for (int i = 0; i < drawCards.count(); i++) {
            duel.playerHands[drawCards.playerIndex()].cards.add(new Card());
        }
    }
}
