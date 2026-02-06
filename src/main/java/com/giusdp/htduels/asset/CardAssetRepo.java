package com.giusdp.htduels.asset;
import com.giusdp.htduels.match.CardRepo;
import com.giusdp.htduels.DuelsPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class CardAssetRepo implements CardRepo {
    @Override
    public Collection<CardAsset> getAvailableCards() {
        return new ArrayList<>(DuelsPlugin.cardAssetStore.getAssetMap().getAssetMap().values());
    }

    @Override
    public Optional<CardAsset> findById(String cardAssetId) {
        if (cardAssetId == null) {
            return Optional.empty();
        }
        CardAsset asset = DuelsPlugin.cardAssetStore.getAssetMap().getAsset(cardAssetId);
        return Optional.ofNullable(asset);
    }
}
