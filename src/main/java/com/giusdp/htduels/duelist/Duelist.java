package com.giusdp.htduels.duelist;

import com.giusdp.htduels.asset.CardAsset;

import java.util.ArrayList;
import java.util.List;

public abstract class Duelist {
    private final List<CardAsset> hand;

    protected Duelist() {
        this.hand = new ArrayList<>();
    }

    public List<CardAsset> getHand() {
        return hand;
    }

    public void addToHand(CardAsset card) {
        hand.add(card);
    }

    public void removeFromHand(CardAsset card) {
        hand.remove(card);
    }
}