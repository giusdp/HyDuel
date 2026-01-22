package com.giusdp.htduels.duel;

import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.duelist.Duelist;
import com.hypixel.hytale.math.Vec2f;

public class Card {
    private final CardAsset asset;
    private Duelist owner;

    Vec2f position;

    public Card(CardAsset asset) {
        this.asset = asset;
        position = new Vec2f(0, 0);
    }

    public CardAsset getAsset() {
        return asset;
    }
}
