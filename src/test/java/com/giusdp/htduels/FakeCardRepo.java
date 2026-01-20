package com.giusdp.htduels;

import com.giusdp.htduels.asset.CardAsset;

import java.util.Collection;
import java.util.List;

public class FakeCardRepo implements CardRepo {

    private static final List<CardAsset> TEST_CARDS = List.of(
        new CardAsset("test_card_1", "Test Card 1", 1, 1, 1, "Minion"),
        new CardAsset("test_card_2", "Test Card 2", 2, 2, 2, "Minion"),
        new CardAsset("test_card_3", "Test Card 3", 3, 3, 3, "Minion")
    );

    @Override
    public Collection<CardAsset> getAvailableCards() {
        return TEST_CARDS;
    }
}