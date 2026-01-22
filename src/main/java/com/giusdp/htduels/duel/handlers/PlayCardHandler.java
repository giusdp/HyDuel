package com.giusdp.htduels.duel.handlers;

import com.giusdp.htduels.duel.event.DuelEvent;
import com.giusdp.htduels.duel.event.PlayCard;

public class PlayCardHandler extends DuelEventHandler {
    @Override
    public void accept(DuelEvent ev) {
        PlayCard playCard = (PlayCard) ev;
        playCard.duelist.playCard(playCard.card);
    }
}