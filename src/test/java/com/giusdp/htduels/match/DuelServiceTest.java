package com.giusdp.htduels.match;

import com.giusdp.htduels.FakeCardRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DuelServiceTest {

    private DuelService duelService;
    private DuelRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new DuelRegistry();
        duelService = new DuelService(registry);
    }

    private Duel createDuelWithFakeRepo() {
        return Duel.builder()
                .cardRepo(new FakeCardRepo())
                .build();
    }

    @Test
    @DisplayName("addHumanDuelist with cardIds initializes deck")
    void addHumanDuelistWithCardIdsInitializesDeck() {
        Duel duel = createDuelWithFakeRepo();
        List<String> cardIds = List.of("test_card_1", "test_card_2", "test_card_3");

        Duelist duelist = duelService.addHumanDuelist(duel, cardIds);

        assertEquals(3, duelist.getDeck().size());
    }

    @Test
    @DisplayName("addBotDuelist with cardIds initializes deck")
    void addBotDuelistWithCardIdsInitializesDeck() {
        Duel duel = createDuelWithFakeRepo();
        List<String> cardIds = List.of("test_card_1", "test_card_2");

        Duelist duelist = duelService.addBotDuelist(duel, cardIds);

        assertEquals(2, duelist.getDeck().size());
    }

    @Test
    @DisplayName("invalid cardIds are skipped during deck creation")
    void invalidCardIdsAreSkipped() {
        Duel duel = createDuelWithFakeRepo();
        List<String> cardIds = List.of("test_card_1", "invalid_card", "test_card_2");

        Duelist duelist = duelService.addHumanDuelist(duel, cardIds);

        assertEquals(2, duelist.getDeck().size()); // Only valid cards added
    }
}
