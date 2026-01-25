package com.giusdp.htduels;

import com.giusdp.htduels.asset.CardAsset;

import java.util.Collection;

public interface CardRepo {
    Collection<CardAsset> getAvailableCards();
}
