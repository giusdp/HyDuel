package com.giusdp.htduels.duel;

import com.giusdp.htduels.CardRepo;
import com.giusdp.htduels.DuelistContext;
import com.giusdp.htduels.duel.event.*;
import com.giusdp.htduels.duel.eventbus.GameEventBus;
import com.giusdp.htduels.duel.handler.*;
import com.giusdp.htduels.duel.phases.StartupPhase;
import com.giusdp.htduels.duelist.Duelist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Duel {
    private final List<Duelist> duelists;

    public final GameEventBus eventBus;
    public final CardRepo cardRepo;

    public Phase currentPhase;
    public Duelist activeDuelist;
    private final List<DuelistContext> contexts = new ArrayList<>();

    public static DuelBuilder builder() {
        return new DuelBuilder();
    }

    Duel(Duelist duelist1, Duelist duelist2, GameEventBus eventBus, CardRepo cardRepo) {
        this.duelists = List.of(duelist1, duelist2);
        this.eventBus = eventBus;
        this.cardRepo = cardRepo;
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
        this.eventBus.register(eventType, this, (short) 0, handler);
    }

    @SuppressWarnings("unchecked")
    public <T extends DuelEvent> void emit(T event) {
        this.eventBus.post((Class<T>) event.getClass(), this, event);
    }

    public void setActiveDuelist(Duelist duelist) {
        this.activeDuelist = duelist;
    }

    public Duelist getDuelist(int index) {
        return duelists.get(index);
    }

    public List<Duelist> getDuelists() {
        return duelists;
    }

    public void addContext(DuelistContext ctx) {
        contexts.add(ctx);
    }

    public List<DuelistContext> getContexts() {
        return Collections.unmodifiableList(contexts);
    }

    public void swapActiveDuelist() {
        for (Duelist d : duelists) {
            if (d != activeDuelist) {
                this.activeDuelist = d;
                return;
            }
        }
    }
}
