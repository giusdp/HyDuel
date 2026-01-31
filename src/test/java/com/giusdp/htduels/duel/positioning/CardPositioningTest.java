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
        duel = new Duel(new DuelPlayer(), new Bot(), new FakeEventBus(), new FakeCardRepo());
    }

    @Test
    void duelistSetsOwnerOnCard() {
        Card card = new Card(new CardAsset("test", "Test Card", 1, 2, 3, "Minion"));
        duel.getDuelist(0).addToHand(card);

        assertNotNull(card.getOwner());
        assertSame(duel.getDuelist(0), card.getOwner());
    }

    @Test
    void cardPositioningServiceReturnsPositionForCardInHand() {
        Card card = new Card(new CardAsset("test", "Test Card", 1, 2, 3, "Minion"));
        duel.getDuelist(0).addToHand(card);

        var position = CardPositioningService.getWorldPosition(card, boardLayout);

        assertNotNull(position);
    }

    @Test
    void cardPositioningServiceReturnsPositionForCardOnBattlefield() {
        Card card = new Card(new CardAsset("test", "Test Card", 1, 2, 3, "Minion"));
        duel.getDuelist(0).playCard(card);

        var position = CardPositioningService.getWorldPosition(card, boardLayout);

        assertNotNull(position);
    }
}