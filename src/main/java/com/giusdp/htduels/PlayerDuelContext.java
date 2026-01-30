package com.giusdp.htduels;


import com.giusdp.htduels.duelist.Duelist;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.protocol.Position;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import com.hypixel.hytale.math.Vec2f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerDuelContext {
    public record DuelSpatialData(Position cameraPos, float cameraYaw, float cardY) {}

    final DuelSpatialData spatialData;

    private static final Map<PlayerRef, PlayerDuelContext> sessions = new HashMap<>();

    public static void register(PlayerRef player, Ref<EntityStore> duelRef, Duelist duelist, Position cameraPos, float cameraYaw, float cardY) {
        sessions.put(player, new PlayerDuelContext(player, duelRef, duelist, cameraPos, cameraYaw, cardY));
    }

    @Nullable
    public static PlayerDuelContext get(PlayerRef player) {
        return sessions.get(player);
    }

    public static List<PlayerDuelContext> getByDuelRef(Ref<EntityStore> duelRef) {
        List<PlayerDuelContext> result = new ArrayList<>();
        for (PlayerDuelContext ctx : sessions.values()) {
            if (ctx.duelRef.equals(duelRef)) {
                result.add(ctx);
            }
        }
        return result;
    }

    public static void unregisterByDuelRef(Ref<EntityStore> duelRef) {
        sessions.values().removeIf(ctx -> ctx.duelRef.equals(duelRef));
    }

    private final PlayerRef playerRef;
    private final Ref<EntityStore> duelRef;
    private final Duelist duelist;
    private final List<Ref<EntityStore>> cardRefs = new ArrayList<>();
    private @Nullable Vec2f mouseWorldPosition;
    private @Nullable Ref<EntityStore> draggedCard;

    public PlayerDuelContext(PlayerRef playerRef, Ref<EntityStore> duelRef, Duelist duelist, Position cameraPos, float cameraYaw, float cardY) {
        this.playerRef = playerRef;
        this.duelRef = duelRef;
        this.duelist = duelist;
        this.spatialData = new DuelSpatialData(cameraPos, cameraYaw, cardY);
    }

    public PlayerRef getPlayerRef() {
        return playerRef;
    }

    public Ref<EntityStore> getDuelRef() {
        return duelRef;
    }

    public Duelist getDuelist() {
        return duelist;
    }

    public DuelSpatialData getSpatialData() {return spatialData;}

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

    public void setMouseWorldPosition(Vec2f mouseWorldPosition) {
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
