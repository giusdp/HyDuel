package com.giusdp.htduels.duel;

import com.giusdp.htduels.CardRepo;
import com.giusdp.htduels.duel.handlers.DrawCardsHandler;
import com.giusdp.htduels.duel.handlers.DrawCardsLogHandler;
import com.giusdp.htduels.duel.handlers.DuelStartedLogHandler;
import com.giusdp.htduels.duel.event.DrawCards;
import com.giusdp.htduels.duel.event.DuelStarted;
import com.giusdp.htduels.duel.event.DuelEvent;
import com.giusdp.htduels.duel.event_bus.GameEventBus;
import com.giusdp.htduels.duel.phases.StartupPhase;

public class Duel {
    public Hand[] playerHands;
    public Phase currentPhase;
    private final GameEventBus eventBus;

    public Duel(GameEventBus eventBus, CardRepo cardRepo) {
        this.eventBus = eventBus;
        playerHands = new Hand[2];
        playerHands[0] = new Hand();
        playerHands[1] = new Hand();

        registerHandler(DrawCards.class, new DrawCardsHandler(this, cardRepo));
        registerHandler(DrawCards.class, new DrawCardsLogHandler());
        registerHandler(DuelStarted.class, new DuelStartedLogHandler());

        emit(new DuelStarted());
        currentPhase = new StartupPhase();
        currentPhase.onEnter(this);
    }

    public void tick() {
        currentPhase.tick(this);
    }

    public void transitionTo(Phase newPhase) {
        currentPhase.onExit(this);
        currentPhase = newPhase;
        currentPhase.onEnter(this);
    }

    public <T extends DuelEvent> void registerHandler(Class<T> eventType, DuelEventHandler handler) {
        eventBus.register(eventType, (short) 0, handler);
    }

    @SuppressWarnings("unchecked")
    public <T extends DuelEvent> void emit(T event) {
        eventBus.post((Class<T>) event.getClass(), null, event);
    }
}
