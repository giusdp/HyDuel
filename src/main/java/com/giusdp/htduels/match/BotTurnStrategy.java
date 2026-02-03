package com.giusdp.htduels.match;

import com.giusdp.htduels.match.Card;
import com.giusdp.htduels.match.Duel;

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
