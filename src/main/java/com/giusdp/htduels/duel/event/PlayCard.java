package com.giusdp.htduels.duel.event;

import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.duelist.Duelist;

public record PlayCard(Duelist duelist, CardAsset card) implements DuelEvent {
}
