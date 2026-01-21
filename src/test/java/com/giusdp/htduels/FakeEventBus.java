package com.giusdp.htduels;

import com.giusdp.htduels.duel.event.DuelEvent;
import com.giusdp.htduels.duel.eventbus.GameEventBus;
import com.giusdp.htduels.duel.handlers.DuelEventHandler;
import com.hypixel.hytale.event.IEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeEventBus implements GameEventBus {
  private final List<DuelEvent> posted = new ArrayList<>();
  private final Map<Class<?>, List<DuelEventHandler>> handlers = new HashMap<>();

  @Override
  public <K, E extends IEvent<K>> void post(Class<E> eventClass, K key, E event) {
    DuelEvent duelEvent = (DuelEvent) event;
    posted.add(duelEvent);

    var list = handlers.get(eventClass);
    if (list != null) {
      for (var h : list) {
        h.accept(duelEvent);
      }
    }
  }

  @Override
  public <K, E extends IEvent<K>> void register(Class<E> eventClass, short priority, DuelEventHandler handler) {
    handlers.computeIfAbsent(eventClass, k -> new ArrayList<>()).add(handler);
  }

  public List<DuelEvent> postedEvents() {
    return posted;
  }
}
