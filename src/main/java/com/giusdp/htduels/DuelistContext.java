package com.giusdp.htduels;


import com.giusdp.htduels.duelist.Duelist;
import com.giusdp.htduels.ui.BoardGameUi;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.protocol.Position;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import com.hypixel.hytale.math.Vec2f;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DuelistContext {
    public record DuelSpatialData(Position cameraPos, float cameraYaw, float cardY) {}

    private static final Map<PlayerRef, DuelistContext> playerRegistry = new HashMap<>();
    final @Nullable DuelSpatialData spatialData;

    private final @Nullable PlayerRef playerRef;
    private final Ref<EntityStore> duelRef;
    private final Duelist duelist;
    private final List<Ref<EntityStore>> cardRefs = new ArrayList<>();
    private @Nullable BoardGameUi boardGameUi;
    private @Nullable Vec2f mouseWorldPosition;
    private @Nullable Ref<EntityStore> draggedCard;

    public static DuelistContext registerGlobal(PlayerRef player, Ref<EntityStore> duelRef, Duelist duelist, Position cameraPos, float cameraYaw, float cardY) {
        DuelistContext ctx = new DuelistContext(player, duelRef, duelist, cameraPos, cameraYaw, cardY);
        playerRegistry.put(player, ctx);
        return ctx;
    }

    @Nullable
    public static DuelistContext get(PlayerRef player) {
        return playerRegistry.get(player);
    }

    public static void unregisterByDuelRef(Ref<EntityStore> duelRef) {
        playerRegistry.values().removeIf(ctx -> ctx.duelRef.equals(duelRef));
    }


    public DuelistContext(PlayerRef playerRef, Ref<EntityStore> duelRef, Duelist duelist, Position cameraPos, float cameraYaw, float cardY) {
        this.playerRef = playerRef;
        this.duelRef = duelRef;
        this.duelist = duelist;
        this.spatialData = new DuelSpatialData(cameraPos, cameraYaw, cardY);
    }

    public DuelistContext(Ref<EntityStore> duelRef, Duelist duelist) {
        this.playerRef = null;
        this.duelRef = duelRef;
        this.duelist = duelist;
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
