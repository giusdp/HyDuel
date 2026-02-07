package com.giusdp.htduels.asset;

import com.giusdp.htduels.FakeCardRepo;
import com.giusdp.htduels.match.CardRepo;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CardRepoTest {

    private final CardRepo cardRepo = new FakeCardRepo();

    @Test
    void findByIdReturnsCardWhenExists() {
        Optional<CardAsset> result = cardRepo.findById("test_card_1");

        assertTrue(result.isPresent());
        assertEquals("test_card_1", result.get().id());
        assertEquals("Test Card 1", result.get().name());
    }

    @Test
    void findByIdReturnsEmptyWhenNotFound() {
        Optional<CardAsset> result = cardRepo.findById("nonexistent_card");

        assertTrue(result.isEmpty());
    }

    @Test
    void findByIdReturnsEmptyForNullId() {
        Optional<CardAsset> result = cardRepo.findById(null);

        assertTrue(result.isEmpty());
    }
}
