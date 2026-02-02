package com.giusdp.htduels.presentation;


import com.giusdp.htduels.match.Duel;
import com.giusdp.htduels.match.Duelist;
import com.giusdp.htduels.presentation.ui.BoardGameUi;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.protocol.Position;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import com.hypixel.hytale.math.Vec2f;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DuelistSessionManager {
    public record DuelSpatialData(Position cameraPos, float cameraYaw, float cardY) {}

    final @Nullable DuelSpatialData spatialData;

    private final @Nullable PlayerRef playerRef;
    private final Ref<EntityStore> duelRef;
    private final Duelist duelist;
    private final Duel duel;
    private final List<Ref<EntityStore>> cardRefs = new ArrayList<>();
    private @Nullable BoardGameUi boardGameUi;
    private @Nullable Vec2f mouseWorldPosition;
    private @Nullable Ref<EntityStore> draggedCard;

    public DuelistSessionManager(@Nullable PlayerRef playerRef, Ref<EntityStore> duelRef, Duelist duelist, Duel duel, Position cameraPos, float cameraYaw, float cardY) {
        this.playerRef = playerRef;
        this.duelRef = duelRef;
        this.duelist = duelist;
        this.duel = duel;
        this.spatialData = new DuelSpatialData(cameraPos, cameraYaw, cardY);
    }

    public DuelistSessionManager(Ref<EntityStore> duelRef, Duelist duelist, Duel duel) {
        this.playerRef = null;
        this.duelRef = duelRef;
        this.duelist = duelist;
        this.duel = duel;
        this.spatialData = null;
    }

    @Nullable
    public PlayerRef getPlayerRef() {
        return playerRef;
    }

    public Ref<EntityStore> getDuelRef() {
        return duelRef;
    }

    public Duelist getDuelist() {
        return duelist;
    }

    public Duel getDuel() {
        return duel;
    }

    @Nullable
    public DuelSpatialData getSpatialData() {return spatialData;}

    @Nullable
    public BoardGameUi getBoardGameUi() {
        return boardGameUi;
    }

    public void setBoardGameUi(@NotNull BoardGameUi boardGameUi) {
        this.boardGameUi = boardGameUi;
    }

    public List<Ref<EntityStore>> getCardEntities() {
        return cardRefs;
    }

    public void addCardEntity(Ref<EntityStore> cardRef) {
        cardRefs.add(cardRef);
    }

    @Nullable
    public Vec2f getMouseWorldPosition() {
        return mouseWorldPosition;
    }

    public void setMouseWorldPosition(@NotNull Vec2f mouseWorldPosition) {
        this.mouseWorldPosition = mouseWorldPosition;
    }

    @Nullable
    public Ref<EntityStore> getDraggedCard() {
        return draggedCard;
    }

    public void setDraggedCard(@Nullable Ref<EntityStore> draggedCard) {
        this.draggedCard = draggedCard;
    }
}
