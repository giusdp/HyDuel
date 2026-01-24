package com.giusdp.htduels.duel;

import com.giusdp.htduels.CardRepo;
import com.giusdp.htduels.duel.event.*;
import com.giusdp.htduels.duel.eventbus.GameEventBus;
import com.giusdp.htduels.duel.handlers.*;
import com.giusdp.htduels.duel.phases.StartupPhase;
import com.giusdp.htduels.duel.positioning.BoardLayout;
import com.giusdp.htduels.duelist.Duelist;

public class Duel {
    public final Duelist duelist1;
    public final Duelist duelist2;

    public final GameEventBus eventBus;
    public final CardRepo cardRepo;
    public final BoardLayout boardLayout;

    public Phase currentPhase;
    public Duelist activeDuelist;

    public Duel(Duelist duelist1, Duelist duelist2, GameEventBus eventBus, CardRepo cardRepo, BoardLayout boardLayout) {
        this.duelist1 = duelist1;
        this.duelist2 = duelist2;
        this.eventBus = eventBus;
        this.cardRepo = cardRepo;
        this.boardLayout = boardLayout;
    }

    public void setup() {
        currentPhase = new StartupPhase();

        registerHandler(DrawCards.class, new DrawCardsHandler(this, cardRepo));

        registerHandler(DrawCards.class, new DrawCardsLogHandler());

        registerHandler(PlayCard.class, new PlayCardHandler());
        registerHandler(RandomDuelistSelect.class, new RandomDuelistSelectHandler());
        registerHandler(EndMainPhase.class, new EndMainPhaseHandler());

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
        this.eventBus.register(eventType, (short) 0, handler);
    }

    @SuppressWarnings("unchecked")
    public <T extends DuelEvent> void emit(T event) {
        this.eventBus.post((Class<T>) event.getClass(), null, event);
    }

    public void setActiveDuelist(Duelist duelist) {
        this.activeDuelist = duelist;
    }

    public void swapActiveDuelist() {
        if (this.activeDuelist == duelist1) {
            this.activeDuelist = duelist2;
        } else {
            this.activeDuelist = duelist1;
        }
    }
}
