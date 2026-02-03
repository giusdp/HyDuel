package com.giusdp.htduels;
import com.giusdp.htduels.match.CardRepo;

import com.giusdp.htduels.catalog.CardAsset;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class FakeCardRepo implements CardRepo {

    private static final List<CardAsset> TEST_CARDS = List.of(
            new CardAsset("test_card_1", "Test Card 1", 1, 1, 1, "Minion"),
            new CardAsset("test_card_2", "Test Card 2", 2, 2, 2, "Minion"),
            new CardAsset("test_card_3", "Test Card 3", 3, 3, 3, "Minion"));

    @Override
    public Collection<CardAsset> getAvailableCards() {
        return TEST_CARDS;
    }

    @Override
    public Optional<CardAsset> findById(String cardAssetId) {
        if (cardAssetId == null) {
            return Optional.empty();
        }
        return TEST_CARDS.stream()
                .filter(card -> card.id().equals(cardAssetId))
                .findFirst();
    }
}
