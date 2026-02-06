package com.giusdp.htduels.match;

import com.giusdp.htduels.asset.CardAsset;

import java.util.Collection;
import java.util.Optional;

public interface CardRepo {
    Collection<CardAsset> getAvailableCards();
    Optional<CardAsset> findById(String cardAssetId);
}
