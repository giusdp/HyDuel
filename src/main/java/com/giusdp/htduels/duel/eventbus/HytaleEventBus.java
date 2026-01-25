package com.giusdp.htduels.duel.eventbus;

import com.giusdp.htduels.duel.handlers.DuelEventHandler;
import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.server.core.HytaleServer;

import java.util.function.Consumer;

public final class HytaleEventBus implements GameEventBus {
    @Override
    public <K, E extends IEvent<K>> void post(Class<E> eventClass, K key, E event) {
        HytaleServer.get().getEventBus().dispatchFor(eventClass, key).dispatch(event);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K, E extends IEvent<K>> void register(Class<E> eventClass, short priority, DuelEventHandler handler) {
        HytaleServer.get().getEventBus().registerGlobal(priority, eventClass, (Consumer<E>) handler);
    }
}
