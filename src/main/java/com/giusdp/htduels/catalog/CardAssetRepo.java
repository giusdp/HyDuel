package com.giusdp.htduels.catalog;
import com.giusdp.htduels.match.CardRepo;
import com.giusdp.htduels.DuelsPlugin;

import com.giusdp.htduels.catalog.CardAsset;

import java.util.ArrayList;
import java.util.Collection;

public class CardAssetRepo implements CardRepo {
    @Override
    public Collection<CardAsset> getAvailableCards() {
        return new ArrayList<>(DuelsPlugin.cardAssetStore.getAssetMap().getAssetMap().values());
    }
}
