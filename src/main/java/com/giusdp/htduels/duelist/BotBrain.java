package com.giusdp.htduels.duelist;

import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.event.PlayCard;

import java.util.List;

public class BotBrain {
    private final Bot bot;

    public BotBrain(Bot bot) {
        this.bot = bot;
    }

    public void takeTurn(Duel duel) {
        List<CardAsset> hand = bot.getHand();
        if (hand.isEmpty()) {
            return;
        }

        CardAsset cardToPlay = hand.getFirst();
        duel.emit(new PlayCard(bot, cardToPlay));
    }
}
