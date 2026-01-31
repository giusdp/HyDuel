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

        duel.getDuelist(0).getHand().getCards().clear();
        assertEquals(0, duel.getDuelist(0).getHand().getCards().size());

        duel.emit(new DrawCards(duel, duel.getDuelist(0), 5));
        assertEquals(5, duel.getDuelist(0).getHand().getCards().size());

        duel.emit(new DrawCards(duel, duel.getDuelist(0), 1));
        assertEquals(6, duel.getDuelist(0).getHand().getCards().size());
    }
}
