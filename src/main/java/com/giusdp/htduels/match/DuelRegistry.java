package com.giusdp.htduels.match;

import com.giusdp.htduels.match.Duel;
import com.giusdp.htduels.match.DuelId;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class DuelRegistry {

    private final Map<Vector3i, Ref<EntityStore>> activeDuels = new HashMap<>();
    private final Map<DuelId, Duel> duelsByIdMap = new HashMap<>();

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
