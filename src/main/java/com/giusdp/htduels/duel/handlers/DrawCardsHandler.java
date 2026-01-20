package com.giusdp.htduels.duel.handlers;

import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.MoveHandler;
import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.duel.moves.DrawCards;
import com.giusdp.htduels.duel.moves.Move;

import java.util.List;
import java.util.function.Supplier;

public class DrawCardsHandler implements MoveHandler {

    private static final CardAsset PLACEHOLDER = new CardAsset("Placeholder", "Placeholder", 0, 1, 1, "Minion");

    // Set by DuelsPlugin at startup
    public static Supplier<List<CardAsset>> cardSource = List::of;

    @Override
    public void handle(Move move, Duel duel) {
        DrawCards drawCards = (DrawCards) move;
        List<CardAsset> availableCards = cardSource.get();

        for (int i = 0; i < drawCards.count(); i++) {
            CardAsset card = availableCards.isEmpty()
                ? PLACEHOLDER
                : availableCards.get(i % availableCards.size());
            duel.playerHands[drawCards.playerIndex()].cards.add(card);
        }
    }
}
