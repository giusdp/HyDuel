package com.giusdp.htduels;

import com.giusdp.htduels.asset.CardAsset;

import java.util.ArrayList;
import java.util.Collection;

public class CardAssetRepo implements CardRepo {
    @Override
    public Collection<CardAsset> getAvailableCards() {
        return new ArrayList<>(DuelsPlugin.cardAssetStore.getAssetMap().getAssetMap().values());
    }
}