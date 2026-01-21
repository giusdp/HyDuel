package com.giusdp.htduels.duel.event;

import com.giusdp.htduels.duelist.Duelist;

public record DrawCards(Duelist duelist, int count) implements DuelEvent {
}
