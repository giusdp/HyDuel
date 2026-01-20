package com.giusdp.htduels.duel.event_bus;

import com.hypixel.hytale.event.IEvent;

import java.util.function.Consumer;

public interface GameEventBus {
    <K, E extends IEvent<K>> void post(Class<E> eventClass, K key, E event);

    <K, E extends IEvent<K>> void register(
            Class<E> eventClass,
            short priority,
            Consumer<E> handler
    );
}
