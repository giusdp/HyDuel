package com.giusdp.htduels.duelist;

import com.giusdp.htduels.duel.Card;
import com.giusdp.htduels.duel.Duel;

public class BotTurnStrategy implements TurnStrategy {
    @Override
    public void takeTurn(Duel duel, Duelist self) {
        if (self.getHand().getCards().isEmpty()) {
            duel.endMainPhase();
            return;
        }
        Card cardToPlay = self.getHand().getCards().getFirst();
        duel.playCard(self, cardToPlay);
        duel.endMainPhase();
    }
}
