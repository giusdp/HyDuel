package com.giusdp.htduels.duel.handlers;

import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.event.DuelEvent;
import java.util.function.Consumer;
import org.jspecify.annotations.NonNull;

public abstract class DuelEventHandler implements Consumer<DuelEvent> {
  @Override
  public @NonNull Consumer<DuelEvent> andThen(@NonNull Consumer<? super DuelEvent> after) {
    return Consumer.super.andThen(after);
  }
}
