package com.giusdp.htduels.duel.event;

import com.giusdp.htduels.duel.CardId;
import com.giusdp.htduels.duel.DuelId;
import org.jspecify.annotations.NonNull;

public class CardPlayed extends DuelEvent {
    public final CardId cardId;

    public CardPlayed(@NonNull DuelId duelId, CardId cardId) {
        super(duelId);
        this.cardId = cardId;
    }
}
