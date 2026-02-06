package com.giusdp.htduels;

import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.match.Card;
import com.giusdp.htduels.match.Duel;
import com.giusdp.htduels.match.phases.StartupPhase;
import com.giusdp.htduels.match.phases.TurnStartPhase;
import com.giusdp.htduels.match.phases.WaitingPhase;
import com.giusdp.htduels.match.BotTurnStrategy;
import com.giusdp.htduels.match.Duelist;
import com.giusdp.htduels.match.HumanTurnStrategy;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DuelTest {

    private List<Card> createTestDeck() {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            cards.add(new Card(new CardAsset("card" + i, "Test Card " + i, 1, 1, 1, "Minion")));
        }
        return cards;
    }

    @Test
    void setupWithTwoDuelistsSkipsWaitingPhase() {
        Duel duel = Duel.builder()
                .cardRepo(new FakeCardRepo())
                .addDuelist(new Duelist(new HumanTurnStrategy()), false)
                .addDuelist(new Duelist(new BotTurnStrategy()), true)
                .build();
        duel.setup();
        assertTrue(duel.isInPhase(StartupPhase.class));
    }

    @Test
    void setupWithZeroDuelistsStaysInWaitingPhase() {
        Duel duel = Duel.builder()
                .cardRepo(new FakeCardRepo())
                .build();
        duel.setup();
        assertTrue(duel.isInPhase(WaitingPhase.class));
    }

    @Test
    void addingSecondDuelistTransitionsToStartup() {
        Duel duel = Duel.builder()
                .cardRepo(new FakeCardRepo())
                .build();
        duel.setup();
        duel.addDuelist(new Duelist(new HumanTurnStrategy()));
        assertTrue(duel.isInPhase(WaitingPhase.class));

        duel.addDuelist(new Duelist(new BotTurnStrategy()));
        assertTrue(duel.isInPhase(StartupPhase.class));
    }

    @Test
    void fullFlowToTurnStart() {
        Duelist duelist0 = new Duelist(new HumanTurnStrategy());
        Duelist duelist1 = new Duelist(new BotTurnStrategy());
        duelist0.initializeDeck(createTestDeck());
        duelist1.initializeDeck(createTestDeck());
        Duel duel = Duel.builder()
                .cardRepo(new FakeCardRepo())
                .addDuelist(duelist0, false)
                .addDuelist(duelist1, true)
                .build();
        duel.setup();

        // 10 ticks in StartupPhase (draw cards)
        for (int i = 0; i < 10; i++) {
            duel.tick();
            assertTrue(duel.isInPhase(StartupPhase.class));
        }
        duel.tick();
        assertTrue(duel.isInPhase(TurnStartPhase.class));
    }

    @Test
    void handsGetFilledOnStartup() {
        Duelist duelist0 = new Duelist(new HumanTurnStrategy());
        Duelist duelist1 = new Duelist(new BotTurnStrategy());
        duelist0.initializeDeck(createTestDeck());
        duelist1.initializeDeck(createTestDeck());
        Duel duel = Duel.builder()
                .cardRepo(new FakeCardRepo())
                .addDuelist(duelist0, false)
                .addDuelist(duelist1, true)
                .build();
        duel.setup();

        for (int i = 0; i < 10; i++) {
            duel.tick();
        }
        assertEquals(5, duel.getDuelist(0).getHand().getCards().size());
        assertEquals(5, duel.getDuelist(1).getHand().getCards().size());
    }
}
