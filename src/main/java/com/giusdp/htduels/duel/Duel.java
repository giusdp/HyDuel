package com.giusdp.htduels.duel;

import com.giusdp.htduels.CardRepo;
import com.giusdp.htduels.DuelistContext;
import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.duel.event.*;
import com.giusdp.htduels.duel.phases.DuelEndPhase;
import com.giusdp.htduels.duel.phases.TurnEndPhase;
import com.giusdp.htduels.duel.phases.WaitingPhase;
import com.giusdp.htduels.duelist.Duelist;

import com.hypixel.hytale.math.vector.Vector3i;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Duel {
    private static final CardAsset PLACEHOLDER = new CardAsset("Placeholder", "Placeholder", 0, 1, 1, "Minion");

    private final DuelId id;
    private final List<Duelist> duelists;
    private final CardRepo cardRepo;

    private Phase currentPhase;
    private Duelist activeDuelist;
    private final List<DuelEvent> accumulatedEvents = new ArrayList<>();
    private final Map<CardId, Card> cardIndex = new HashMap<>();
    private final List<DuelistContext> contexts = new ArrayList<>();
    private Vector3i boardPosition;

    public static DuelBuilder builder() {
        return new DuelBuilder();
    }

    Duel(DuelId id, List<Duelist> duelists, CardRepo cardRepo) {
        this.id = id;
        this.duelists = new ArrayList<>(duelists);
        this.cardRepo = cardRepo;
    }

    public DuelId getId() {
        return id;
    }

    // --- Event accumulation ---

    public void recordEvent(DuelEvent event) {
        accumulatedEvents.add(event);
    }

    public List<DuelEvent> flushEvents() {
        List<DuelEvent> copy = new ArrayList<>(accumulatedEvents);
        accumulatedEvents.clear();
        return copy;
    }

    public List<DuelEvent> getAccumulatedEvents() {
        return Collections.unmodifiableList(accumulatedEvents);
    }

    // --- Query methods ---

    public boolean isInPhase(Class<? extends Phase> phaseClass) {
        return phaseClass.isInstance(currentPhase);
    }

    public Duelist getActiveDuelist() {
        return activeDuelist;
    }

    public boolean isSetUp() {
        return currentPhase != null;
    }

    public boolean isFinished() {
        return currentPhase instanceof DuelEndPhase;
    }

    public DuelEndPhase.Reason getEndReason() {
        if (currentPhase instanceof DuelEndPhase endPhase) {
            return endPhase.reason;
        }
        return null;
    }

    // --- Domain methods ---

    public void drawCards(Duelist duelist, int count) {
        List<CardId> drawnIds = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Card card = new Card(PLACEHOLDER);
            duelist.addToHand(card);
            drawnIds.add(card.getId());
            cardIndex.put(card.getId(), card);
        }
        recordEvent(new CardsDrawn(this.id, drawnIds));
    }

    public void playCard(Duelist duelist, Card card) {
        duelist.playCard(card);
        recordEvent(new CardPlayed(this.id, card.getId()));
    }

    public void playCard(Duelist duelist, CardId cardId) {
        Card card = findCard(cardId);
        if (card == null) {
            throw new IllegalArgumentException("Card not found: " + cardId);
        }
        playCard(duelist, card);
    }

    public Card findCard(CardId cardId) {
        return cardIndex.get(cardId);
    }

    public void selectStartingDuelist() {
        if (new Random().nextBoolean()) {
            setActiveDuelist(getDuelist(0));
        } else {
            setActiveDuelist(getDuelist(1));
        }
        recordEvent(new StartingDuelistSelected(this.id));
    }

    public void endMainPhase() {
        recordEvent(new MainPhaseEnded(this.id));
        transitionTo(new TurnEndPhase());
    }

    public void forfeit() {
        recordEvent(new MainPhaseEnded(this.id));
        transitionTo(new DuelEndPhase(DuelEndPhase.Reason.FORFEIT));
    }

    public void addDuelist(Duelist duelist) {
        duelists.add(duelist);
        recordEvent(new DuelistJoined(this.id));
        if (currentPhase instanceof WaitingPhase waitingPhase) {
            waitingPhase.onDuelistJoined(this);
        }
    }

    // --- Lifecycle ---

    public void setup() {
        currentPhase = new WaitingPhase();
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

    public void setActiveDuelist(Duelist duelist) {
        this.activeDuelist = duelist;
    }

    public void swapActiveDuelist() {
        for (Duelist d : duelists) {
            if (d != activeDuelist) {
                this.activeDuelist = d;
                return;
            }
        }
    }

    // --- Accessors ---

    public Duelist getDuelist(int index) {
        return duelists.get(index);
    }

    public List<Duelist> getDuelists() {
        return duelists;
    }

    public Vector3i getBoardPosition() {
        return boardPosition;
    }

    public void setBoardPosition(Vector3i boardPosition) {
        this.boardPosition = boardPosition;
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
}
