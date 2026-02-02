package com.giusdp.htduels;

import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.DuelId;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class DuelRegistry {

    private final Map<PlayerRef, DuelistContext> playerSessions = new HashMap<>();
    private final Map<Vector3i, Ref<EntityStore>> activeDuels = new HashMap<>();
    private final Map<DuelId, Duel> duelsByIdMap = new HashMap<>();

    public void registerPlayer(PlayerRef playerRef, DuelistContext ctx) {
        playerSessions.put(playerRef, ctx);
    }

    @Nullable
    public DuelistContext getSession(PlayerRef playerRef) {
        return playerSessions.get(playerRef);
    }

    public void unregisterByDuelRef(Ref<EntityStore> duelRef) {
        playerSessions.values().removeIf(ctx -> ctx.getDuelRef().equals(duelRef));
    }

    public void registerDuel(Vector3i boardPosition, Ref<EntityStore> duelRef) {
        activeDuels.put(boardPosition, duelRef);
    }

    public void registerDuel(DuelId duelId, Duel duel) {
        duelsByIdMap.put(duelId, duel);
    }

    @Nullable
    public Duel findDuel(DuelId duelId) {
        return duelsByIdMap.get(duelId);
    }

    public void removeDuelById(DuelId duelId) {
        duelsByIdMap.remove(duelId);
    }

    @Nullable
    public Ref<EntityStore> findDuelAt(Vector3i boardPosition) {
        return activeDuels.get(boardPosition);
    }

    public void removeDuel(Vector3i boardPosition) {
        activeDuels.remove(boardPosition);
    }
}
