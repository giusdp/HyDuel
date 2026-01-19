package com.giusdp.htduels.duel;

import com.giusdp.htduels.duel.handlers.DrawCardsHandler;
import com.giusdp.htduels.duel.handlers.DrawCardsLogHandler;
import com.giusdp.htduels.duel.handlers.DuelStartedLogHandler;
import com.giusdp.htduels.duel.moves.DrawCards;
import com.giusdp.htduels.duel.moves.DuelStarted;
import com.giusdp.htduels.duel.moves.Move;
import com.giusdp.htduels.duel.phases.StartupPhase;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Duel {

    public Hand[] playerHands;
    public Phase currentPhase;
    private final List<Move> moves = new ArrayList<>();
    private final Map<Class<? extends Move>, List<MoveHandler>> handlers = new HashMap<>();

    public Duel() {
        playerHands = new Hand[2];
        playerHands[0] = new Hand();
        playerHands[1] = new Hand();

        registerHandler(DrawCards.class, new DrawCardsHandler());
        registerHandler(DrawCards.class, new DrawCardsLogHandler());
        registerHandler(DuelStarted.class, new DuelStartedLogHandler());

        emit(new DuelStarted());
        currentPhase = new StartupPhase();
        currentPhase.onEnter(this);
    }

    public void tick() {
        currentPhase.tick(this);
        processMoves();
        clearMoves();
    }

    public void transitionTo(Phase newPhase) {
        currentPhase.onExit(this);
        currentPhase = newPhase;
        currentPhase.onEnter(this);
    }

    public <T extends Move> void registerHandler(Class<T> moveType, MoveHandler handler) {
        handlers.computeIfAbsent(moveType, _ -> new ArrayList<>()).add(handler);
    }

    public void emit(Move move) {
        moves.add(move);
    }

    public List<Move> getMoves() {
        return new ArrayList<>(moves);
    }

    public void clearMoves() {
        moves.clear();
    }

    public void processMoves() {
        moves.forEach(move -> {
            List<MoveHandler> moveHandlers = handlers.get(move.getClass());
            if (moveHandlers != null) {
                moveHandlers.forEach(handler -> handler.handle(move, this));
            }
        });
    }


}
