package com.giusdp.htduels.duel.handler;

import com.giusdp.htduels.duel.event.DuelEvent;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public abstract class DuelEventHandler implements Consumer<DuelEvent> {
    @Override
    public @NonNull Consumer<DuelEvent> andThen(@NonNull Consumer<? super DuelEvent> after) {
        return Consumer.super.andThen(after);
    }
}
