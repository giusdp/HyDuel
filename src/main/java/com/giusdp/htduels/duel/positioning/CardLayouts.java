package com.giusdp.htduels.duel.positioning;

import com.giusdp.htduels.duel.Card;
import com.giusdp.htduels.duel.zone.Battlefield;
import com.giusdp.htduels.duel.zone.Hand;
import com.giusdp.htduels.duelist.Duelist;
import com.hypixel.hytale.math.Vec2f;

public final class CardLayouts {

    private CardLayouts() {
    }

    public static Vec2f battlefield(Card card, BoardLayout board) {
        Duelist owner = card.getOwner();
        Battlefield bf = owner.getBattlefield();

        int index = card.getZoneIndex();
        int count = bf.getCards().size();

        float localX = (index - (count - 1) / 2.0f) * board.battlefieldSpacing();
        float localZ = owner.isBottomPlayer()
                ? -board.playerBattlefieldDepth()
                : board.opponentBattlefieldDepth();

        return board.toWorldPosition(localX, localZ);
    }

    public static Vec2f hand(Card card, BoardLayout board) {
        Duelist owner = card.getOwner();
        Hand hand = owner.getHand();

        int index = card.getZoneIndex();
        int count = hand.getCards().size();

        float localX = (index - (count - 1) / 2.0f) * board.handSpacing();
        float localZ = owner.isBottomPlayer()
                ? -board.playerHandDepth()
                : board.opponentHandDepth();

        return board.toWorldPosition(localX, localZ);
    }

    public static Vec2f deck(Card card, BoardLayout board) {
        Duelist owner = card.getOwner();

        float localZ = owner.isBottomPlayer()
                ? -board.playerHandDepth()
                : board.opponentHandDepth();

        return board.toWorldPosition(-board.deckOffsetX(), localZ);
    }

    public static Vec2f graveyard(Card card, BoardLayout board) {
        Duelist owner = card.getOwner();

        float localZ = owner.isBottomPlayer()
                ? -board.playerHandDepth()
                : board.opponentHandDepth();

        // Graveyard on the opposite side of deck
        return board.toWorldPosition(board.deckOffsetX(), localZ);
    }
}
