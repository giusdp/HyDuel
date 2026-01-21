package com.giusdp.htduels.duel.handlers;

import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.event.DuelEvent;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public abstract class DuelEventHandler implements Consumer<DuelEvent> {
    protected final Duel duel;

    public DuelEventHandler(@NonNull Duel duel) {
        this.duel = duel;
    }

    @Override
    public @NonNull Consumer<DuelEvent> andThen(@NonNull Consumer<? super DuelEvent> after) {
        return Consumer.super.andThen(after);
    }
}
