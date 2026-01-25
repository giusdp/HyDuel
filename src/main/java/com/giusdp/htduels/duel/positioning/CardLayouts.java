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

//        Vec2f center = owner.isBottomPlayer()
//                ? board.playerBottomBattlefieldCenter
//                : board.playerTopBattlefieldCenter;
//
//        float spacing = board.battlefieldSpacing;
//        float offset = (index - (count - 1) / 2.0f) * spacing;
//
//        return center.add(new Vec2f(offset, 0));
        return new Vec2f();
    }

    public static Vec2f hand(Card card, BoardLayout board) {
        Duelist owner = card.getOwner();
        Hand hand = owner.getHand();

        int index = card.getZoneIndex();
        int count = hand.getCards().size();

//        Vec2f center = owner.isBottomPlayer()
//                ? board.playerBottomHandCenter
//                : board.playerTopHandCenter;
//
//        float spacing = board.handSpacing;
//        float offset = (index - (count - 1) / 2.0f) * spacing;
//
//        return center.add(new Vec2f(offset, 0));
        return new Vec2f();
    }

    public static Vec2f deck(Card card, BoardLayout board) {

        return new Vec2f();
    }

    public static Vec2f graveyard(Card card, BoardLayout board) {

        return new Vec2f();
    }
}

