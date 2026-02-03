package com.giusdp.htduels.match.event;

import com.giusdp.htduels.match.CardId;
import com.giusdp.htduels.match.DuelId;
import org.jspecify.annotations.NonNull;

public class CardPlayed extends DuelEvent {
    public final CardId cardId;

    public CardPlayed(@NonNull DuelId duelId, CardId cardId) {
        super(duelId);
        this.cardId = cardId;
    }
}
