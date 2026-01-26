package com.giusdp.htduels.duel.eventbus;

import com.giusdp.htduels.duel.handlers.DuelEventHandler;
import com.hypixel.hytale.event.IEvent;

public interface GameEventBus {
    <K, E extends IEvent<K>> void post(Class<E> eventClass, K key, E event);

    <K, E extends IEvent<K>> void register(Class<E> eventClass, K key, short priority, DuelEventHandler handler);
}
