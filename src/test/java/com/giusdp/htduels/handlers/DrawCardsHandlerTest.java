package com.giusdp.htduels.handlers;

import com.giusdp.htduels.FakeCardRepo;
import com.giusdp.htduels.FakeEventBus;
import com.giusdp.htduels.TestBoardLayout;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.event.DrawCards;
import com.giusdp.htduels.duelist.Bot;
import com.giusdp.htduels.duelist.DuelPlayer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DrawCardsHandlerTest {
    @Test
    void addsCardsToHand() {
        Duel duel = new Duel(new DuelPlayer(), new Bot(), new FakeEventBus(), new FakeCardRepo());
        duel.setup();

        duel.duelist1.getHand().getCards().clear();
        assertEquals(0, duel.duelist1.getHand().getCards().size());

        duel.emit(new DrawCards(duel, duel.duelist1, 5));
        assertEquals(5, duel.duelist1.getHand().getCards().size());

        duel.emit(new DrawCards(duel, duel.duelist1, 1));
        assertEquals(6, duel.duelist1.getHand().getCards().size());
    }
}
