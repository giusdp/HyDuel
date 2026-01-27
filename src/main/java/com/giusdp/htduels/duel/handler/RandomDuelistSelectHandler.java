package com.giusdp.htduels.duel.handler;

import com.giusdp.htduels.duel.event.DuelEvent;

import java.util.Random;

public class RandomDuelistSelectHandler extends DuelEventHandler {

    @Override
    public void accept(DuelEvent ev) {
        if (new Random().nextBoolean()) {
            ev.duel.setActiveDuelist(ev.duel.duelist1);
        } else {
            ev.duel.setActiveDuelist(ev.duel.duelist2);
        }
    }
}
