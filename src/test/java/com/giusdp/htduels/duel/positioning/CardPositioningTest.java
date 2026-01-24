package com.giusdp.htduels.duel.positioning;

import com.giusdp.htduels.FakeCardRepo;
import com.giusdp.htduels.FakeEventBus;
import com.giusdp.htduels.TestBoardLayout;
import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.duel.Card;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duelist.Bot;
import com.giusdp.htduels.duelist.DuelPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardPositioningTest {
    Duel duel;
    BoardLayout boardLayout;

    @BeforeEach
    void setup() {
        boardLayout = TestBoardLayout.create();
        duel = new Duel(new DuelPlayer(), new Bot(), new FakeEventBus(), new FakeCardRepo(), boardLayout);
    }

    @Test
    void duelHasBoardLayout() {
        assertNotNull(duel.boardLayout);
        assertSame(boardLayout, duel.boardLayout);
    }

    @Test
    void cardCanAccessBoardLayoutThroughDuel() {
        Card card = new Card(new CardAsset("test", "Test Card", 1, 2, 3, "Minion"));
        card.setDuel(duel);

        assertNotNull(card.getDuel());
        assertNotNull(card.getDuel().boardLayout);
        assertSame(boardLayout, card.getDuel().boardLayout);
    }

    @Test
    void cardPositioningServiceReturnsPositionForCardInHand() {
        Card card = new Card(new CardAsset("test", "Test Card", 1, 2, 3, "Minion"));
        card.setDuel(duel);
        duel.duelist1.addToHand(card);

        var position = CardPositioningService.getWorldPosition(card, boardLayout);

        assertNotNull(position);
    }

    @Test
    void cardPositioningServiceReturnsPositionForCardOnBattlefield() {
        Card card = new Card(new CardAsset("test", "Test Card", 1, 2, 3, "Minion"));
        card.setDuel(duel);
        duel.duelist1.playCard(card);

        var position = CardPositioningService.getWorldPosition(card, boardLayout);

        assertNotNull(position);
    }
}