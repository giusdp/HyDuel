package com.giusdp.htduels;


import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.protocol.Position;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DuelSession {
    public record DuelSpatialData(Position cameraPos, float cameraYaw, float cardY) {}

    final DuelSpatialData spatialData;

    private static final Map<PlayerRef, DuelSession> sessions = new HashMap<>();

    public static void register(PlayerRef player, Ref<EntityStore> duelRef, Position cameraPos, float cameraYaw, float cardY) {
        sessions.put(player, new DuelSession(player, duelRef, cameraPos, cameraYaw, cardY));
    }

    @Nullable
    public static DuelSession get(PlayerRef player) {
        return sessions.get(player);
    }

    private final PlayerRef player;
    private final Ref<EntityStore> duelRef;
    private final List<Ref<EntityStore>> cardRefs = new ArrayList<>();

    public DuelSession(PlayerRef player, Ref<EntityStore> duelRef, Position cameraPos, float cameraYaw, float cardY) {
        this.player = player;
        this.duelRef = duelRef;
        this.spatialData = new DuelSpatialData(cameraPos, cameraYaw, cardY);
    }

    public PlayerRef getPlayer() {
        return player;
    }

    public Ref<EntityStore> getDuelRef() {
        return duelRef;
    }

    public DuelSpatialData getSpatialData() {return spatialData;}

    public List<Ref<EntityStore>> getCardEntities() {
        return cardRefs;
    }

    public void addCardEntity(Ref<EntityStore> cardRef) {
        cardRefs.add(cardRef);
    }


}
