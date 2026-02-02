package com.giusdp.htduels.duel.event;

import com.giusdp.htduels.duel.CardId;
import com.giusdp.htduels.duel.DuelId;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class CardsDrawn extends DuelEvent {
    public final List<CardId> cardIds;

    public CardsDrawn(@NonNull DuelId duelId, List<CardId> cardIds) {
        super(duelId);
        this.cardIds = cardIds;
    }
}
