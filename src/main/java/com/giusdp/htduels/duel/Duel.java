package com.giusdp.htduels.duel;

import com.giusdp.htduels.CardRepo;
import com.giusdp.htduels.DuelistContext;
import com.giusdp.htduels.duel.event.*;
import com.giusdp.htduels.duel.eventbus.GameEventBus;
import com.giusdp.htduels.duel.handler.*;
import com.giusdp.htduels.duel.phases.WaitingPhase;
import com.giusdp.htduels.duelist.Duelist;

import com.hypixel.hytale.math.vector.Vector3i;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

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
    private final List<Ref<EntityStore>> cardEntities = new ArrayList<>();
    private Vector3i boardPosition;

    public static DuelBuilder builder() {
        return new DuelBuilder();
    }

    Duel(List<Duelist> duelists, GameEventBus eventBus, CardRepo cardRepo) {
        this.duelists = new ArrayList<>(duelists);
        this.eventBus = eventBus;
        this.cardRepo = cardRepo;
    }

    public void addDuelist(Duelist duelist) {
        duelists.add(duelist);
        emit(new DuelistJoined(this, duelist));
    }

    public Vector3i getBoardPosition() {
        return boardPosition;
    }

    public void setBoardPosition(Vector3i boardPosition) {
        this.boardPosition = boardPosition;
    }

    public void setup() {
        currentPhase = new WaitingPhase();

        registerHandler(DrawCards.class, new DrawCardsHandler(this, cardRepo));

        registerHandler(DrawCards.class, new DrawCardsLogHandler());

        registerHandler(PlayCard.class, new PlayCardHandler());
        registerHandler(RandomDuelistSelect.class, new RandomDuelistSelectHandler());
        registerHandler(EndMainPhase.class, new EndMainPhaseHandler());
        registerHandler(DuelistJoined.class, new DuelistJoinedHandler());

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

    /**
     * Updates the turn indicator on every duelist's {@link com.giusdp.htduels.ui.BoardGameUi}.
     * Contexts without a UI instance are silently skipped.
     *
     * @param message the text to display; {@code null} is treated as empty (clears the indicator)
     */
    public void broadcastTurnIndicator(String message) {
        String text = message == null ? "" : message;
        for (DuelistContext ctx : contexts) {
            var ui = ctx.getBoardGameUi();
            if (ui != null) {
                ui.updateTurnIndicator(text);
            }
        }
    }

    /** Clears the turn indicator for all duelists. */
    public void clearTurnIndicator() {
        broadcastTurnIndicator("");
    }

    public void addCardEntity(Ref<EntityStore> cardRef) {
        cardEntities.add(cardRef);
    }

    public List<Ref<EntityStore>> getCardEntities() {
        return Collections.unmodifiableList(cardEntities);
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
