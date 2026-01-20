package com.giusdp.htduels.duel.event_bus;

import com.giusdp.htduels.duel.DuelEventHandler;
import com.hypixel.hytale.event.IEvent;


public interface GameEventBus {
    <K, E extends IEvent<K>> void post(Class<E> eventClass, K key, E event);

    <K, E extends IEvent<K>> void register(Class<E> eventClass, short priority, DuelEventHandler handler);
}
