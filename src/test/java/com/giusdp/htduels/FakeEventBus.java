package com.giusdp.htduels;

import com.giusdp.htduels.duel.event_bus.GameEventBus;
import com.hypixel.hytale.event.IEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class FakeEventBus implements GameEventBus {
    private final List<Object> posted = new ArrayList<>();
    private final Map<Class<?>, List<Consumer<?>>> handlers = new HashMap<>();

    @Override
    public <K, E extends IEvent<K>> void post(Class<E> eventClass, K key, E event) {
        posted.add(event);

        var list = handlers.get(eventClass);
        if (list != null) {
            for (var h : list) {
                ((Consumer<E>) h).accept(event);
            }
        }
    }

    @Override
    public <K, E extends IEvent<K>> void register(Class<E> eventClass, short priority, Consumer<E> handler
    ) {
        handlers.computeIfAbsent(eventClass, k -> new ArrayList<>()).add(handler);
    }

    public List<Object> postedEvents() {
        return posted;
    }
}
