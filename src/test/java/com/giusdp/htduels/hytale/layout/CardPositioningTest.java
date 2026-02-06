package com.giusdp.htduels.hytale.layout;

import com.giusdp.htduels.FakeCardRepo;

import com.giusdp.htduels.TestBoardLayout;
import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.match.Card;
import com.giusdp.htduels.match.Duel;
import com.giusdp.htduels.match.BotTurnStrategy;
import com.giusdp.htduels.match.HumanTurnStrategy;
import com.giusdp.htduels.match.Duelist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardPositioningTest {
    Duel duel;
    BoardLayout boardLayout;

    @BeforeEach
    void setup() {
        boardLayout = TestBoardLayout.create();
        duel = Duel.builder()
                .cardRepo(new FakeCardRepo())
                .addDuelist(new Duelist(new HumanTurnStrategy()), true)
                .addDuelist(new Duelist(new BotTurnStrategy()), false)
                .build();
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

        var position = CardPositioningService.getWorldPosition(
                card.getCurrentZoneType(), card.getZoneIndex(),
                card.getZone().getCards().size(), card.getOwner().isOpponentSide(), boardLayout);

        assertNotNull(position);
    }

    @Test
    void cardPositioningServiceReturnsPositionForCardOnBattlefield() {
        Card card = new Card(new CardAsset("test", "Test Card", 1, 2, 3, "Minion"));
        duel.getDuelist(0).playCard(card);

        var position = CardPositioningService.getWorldPosition(
                card.getCurrentZoneType(), card.getZoneIndex(),
                card.getZone().getCards().size(), card.getOwner().isOpponentSide(), boardLayout);

        assertNotNull(position);
    }
}
