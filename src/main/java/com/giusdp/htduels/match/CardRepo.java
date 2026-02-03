package com.giusdp.htduels.match;

import com.giusdp.htduels.catalog.CardAsset;

import java.util.Collection;

public interface CardRepo {
    Collection<CardAsset> getAvailableCards();
}
