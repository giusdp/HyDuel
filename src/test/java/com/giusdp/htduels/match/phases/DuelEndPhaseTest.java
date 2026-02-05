package com.giusdp.htduels.match.phases;

import com.giusdp.htduels.FakeCardRepo;
import com.giusdp.htduels.catalog.CardAsset;
import com.giusdp.htduels.match.Card;
import com.giusdp.htduels.match.Duel;
import com.giusdp.htduels.match.HumanTurnStrategy;
import com.giusdp.htduels.match.Duelist;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DuelEndPhaseTest {

    private List<Card> createTestDeck() {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            cards.add(new Card(new CardAsset("card" + i, "Test Card " + i, 1, 1, 1, "Minion")));
        }
        return cards;
    }

    private Duel createDuel() {
        Duelist duelist0 = new Duelist(new HumanTurnStrategy());
        Duelist duelist1 = new Duelist(new HumanTurnStrategy());
        duelist0.initializeDeck(createTestDeck());
        duelist1.initializeDeck(createTestDeck());
        return Duel.builder()
                .cardRepo(new FakeCardRepo())
                .addDuelist(duelist0, true)
                .addDuelist(duelist1, false)
                .build();
    }

    @Test
    void forfeitDuringMainPhase() {
        Duel duel = createDuel();
        duel.setup();

        // Tick through StartupPhase (10 draws) + transition tick
        for (int i = 0; i < 11; i++) {
            duel.tick();
        }
        // Now in TurnStartPhase, one more tick transitions to MainPhase
        duel.tick();
        assertTrue(duel.isInPhase(MainPhase.class));

        duel.forfeit();
        assertTrue(duel.isInPhase(DuelEndPhase.class));

        // Tick is a no-op — phase stays the same
        duel.tick();
        assertTrue(duel.isInPhase(DuelEndPhase.class));
    }

    @Test
    void forfeitDuringStartup() {
        Duel duel = createDuel();
        duel.setup();
        assertTrue(duel.isInPhase(StartupPhase.class));

        duel.forfeit();
        assertTrue(duel.isInPhase(DuelEndPhase.class));
    }

    @Test
    void forfeitReasonIsPreserved() {
        Duel duel = createDuel();
        duel.setup();

        duel.forfeit();
        assertEquals(DuelEndPhase.Reason.FORFEIT, duel.getEndReason());
    }

    @Test
    void timeoutReasonIsPreserved() {
        Duel duel = Duel.builder()
                .cardRepo(new FakeCardRepo())
                .build();
        duel.setup();

        duel.transitionTo(new DuelEndPhase(DuelEndPhase.Reason.TIMEOUT));
        assertEquals(DuelEndPhase.Reason.TIMEOUT, duel.getEndReason());
    }

    @Test
    void winReasonIsPreserved() {
        Duel duel = createDuel();
        duel.setup();

        duel.transitionTo(new DuelEndPhase(DuelEndPhase.Reason.WIN));
        assertEquals(DuelEndPhase.Reason.WIN, duel.getEndReason());
    }
}
