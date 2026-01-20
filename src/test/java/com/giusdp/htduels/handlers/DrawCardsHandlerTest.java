package com.giusdp.htduels.handlers;

import com.giusdp.htduels.FakeCardRepo;
import com.giusdp.htduels.FakeEventBus;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.event.DrawCards;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DrawCardsHandlerTest {

    @Test
    void addsCardsToHand() {
        Duel duel = new Duel(new FakeEventBus(), new FakeCardRepo());
        duel.playerHands[0].cards.clear();
        duel.emit(new DrawCards(0, 5));
        assertEquals(5, duel.playerHands[0].cards.size());
    }
}
