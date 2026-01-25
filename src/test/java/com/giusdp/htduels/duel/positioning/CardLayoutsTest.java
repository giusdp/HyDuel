package com.giusdp.htduels.duel.positioning;

import com.giusdp.htduels.FakeCardRepo;
import com.giusdp.htduels.FakeEventBus;
import com.giusdp.htduels.TestBoardLayout;
import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.duel.Card;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duelist.Bot;
import com.giusdp.htduels.duelist.DuelPlayer;
import com.hypixel.hytale.math.Vec2f;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardLayoutsTest {
    Duel duel;
    BoardLayout boardLayout;

    @BeforeEach
    void setup() {
        boardLayout = TestBoardLayout.create();
        duel = new Duel(new DuelPlayer(), new Bot(), new FakeEventBus(), new FakeCardRepo());
        duel.duelist1.setBottomPlayer(true);
        duel.duelist2.setBottomPlayer(false);
    }

    @Test
    void singleCardInHandIsCenteredAtHandCenter() {
        Card card = new Card(new CardAsset("test", "Test Card", 1, 2, 3, "Minion"));
        duel.duelist1.addToHand(card);

        Vec2f position = CardLayouts.hand(card, boardLayout);

        assertEquals(boardLayout.playerBottomHandCenter().x, position.x, 0.001f);
        assertEquals(boardLayout.playerBottomHandCenter().y, position.y, 0.001f);
    }

    @Test
    void multipleCardsInHandAreEvenlySpacedAroundCenter() {
        Card card1 = new Card(new CardAsset("test1", "Test Card 1", 1, 2, 3, "Minion"));
        Card card2 = new Card(new CardAsset("test2", "Test Card 2", 1, 2, 3, "Minion"));
        Card card3 = new Card(new CardAsset("test3", "Test Card 3", 1, 2, 3, "Minion"));
        duel.duelist1.addToHand(card1);
        duel.duelist1.addToHand(card2);
        duel.duelist1.addToHand(card3);

        Vec2f pos1 = CardLayouts.hand(card1, boardLayout);
        Vec2f pos2 = CardLayouts.hand(card2, boardLayout);
        Vec2f pos3 = CardLayouts.hand(card3, boardLayout);

        float spacing = boardLayout.handSpacing();
        float centerY = boardLayout.playerBottomHandCenter().y;

        // Cards are evenly spaced
        assertEquals(spacing, Math.abs(pos2.x - pos1.x), 0.001f);
        assertEquals(spacing, Math.abs(pos3.x - pos2.x), 0.001f);

        // Middle card is at center
        assertEquals(boardLayout.playerBottomHandCenter().x, pos2.x, 0.001f);

        // All cards share same Y position
        assertEquals(centerY, pos1.y, 0.001f);
        assertEquals(centerY, pos2.y, 0.001f);
        assertEquals(centerY, pos3.y, 0.001f);
    }

    @Test
    void bottomPlayerUsesBottomHandCenter() {
        Card card = new Card(new CardAsset("test", "Test Card", 1, 2, 3, "Minion"));
        duel.duelist1.addToHand(card);

        Vec2f position = CardLayouts.hand(card, boardLayout);

        assertEquals(boardLayout.playerBottomHandCenter().y, position.y, 0.001f);
    }

    @Test
    void topPlayerUsesTopHandCenter() {
        Card card = new Card(new CardAsset("test", "Test Card", 1, 2, 3, "Minion"));
        duel.duelist2.addToHand(card);

        Vec2f position = CardLayouts.hand(card, boardLayout);

        assertEquals(boardLayout.playerTopHandCenter().y, position.y, 0.001f);
    }

    @Test
    void twoCardsInHandAreSymmetricallySpacedAroundCenter() {
        Card card1 = new Card(new CardAsset("test1", "Test Card 1", 1, 2, 3, "Minion"));
        Card card2 = new Card(new CardAsset("test2", "Test Card 2", 1, 2, 3, "Minion"));
        duel.duelist1.addToHand(card1);
        duel.duelist1.addToHand(card2);

        Vec2f pos1 = CardLayouts.hand(card1, boardLayout);
        Vec2f pos2 = CardLayouts.hand(card2, boardLayout);

        float spacing = boardLayout.handSpacing();
        float centerX = boardLayout.playerBottomHandCenter().x;

        // Two cards should be spacing apart
        assertEquals(spacing, Math.abs(pos2.x - pos1.x), 0.001f);

        // Cards should be symmetrically placed around center
        assertEquals(centerX, (pos1.x + pos2.x) / 2, 0.001f);
    }
}
