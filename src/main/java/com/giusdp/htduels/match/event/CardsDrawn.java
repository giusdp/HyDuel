package com.giusdp.htduels.match.event;

import com.giusdp.htduels.match.CardId;
import com.giusdp.htduels.match.DuelId;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class CardsDrawn extends DuelEvent {
    public final List<CardId> cardIds;

    public CardsDrawn(@NonNull DuelId duelId, List<CardId> cardIds) {
        super(duelId);
        this.cardIds = cardIds;
    }
}
