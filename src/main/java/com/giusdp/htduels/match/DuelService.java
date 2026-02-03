package com.giusdp.htduels.match;
import com.giusdp.htduels.catalog.CardAssetRepo;

import com.giusdp.htduels.match.Duel;
import com.giusdp.htduels.match.DuelId;
import com.giusdp.htduels.match.BotTurnStrategy;
import com.giusdp.htduels.match.Duelist;
import com.giusdp.htduels.match.HumanTurnStrategy;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nullable;

public class DuelService {

    private final DuelRegistry registry;

    public DuelService(DuelRegistry registry) {
        this.registry = registry;
    }

    public Duel createDuel(Vector3i boardPosition) {
        Duel duel = Duel.builder()
                .cardRepo(new CardAssetRepo())
                .boardPosition(boardPosition)
                .build();
        registry.registerDuel(duel.getId(), duel);
        return duel;
    }

    public Duelist addHumanDuelist(Duel duel) {
        Duelist humanDuelist = new Duelist(new HumanTurnStrategy());
        boolean isOpponentSide = !duel.getDuelists().isEmpty();
        humanDuelist.setOpponentSide(isOpponentSide);
        duel.addDuelist(humanDuelist);
        return humanDuelist;
    }

    public Duelist addBotDuelist(Duel duel) {
        Duelist botDuelist = new Duelist(new BotTurnStrategy());
        boolean isOpponentSide = !duel.getDuelists().isEmpty();
        botDuelist.setOpponentSide(isOpponentSide);
        duel.addDuelist(botDuelist);
        return botDuelist;
    }

    @Nullable
    public Duel findDuel(DuelId duelId) {
        return registry.findDuel(duelId);
    }

    @Nullable
    public Ref<EntityStore> findDuelAt(Vector3i boardPosition) {
        return registry.findDuelAt(boardPosition);
    }

    public void removeDuel(Vector3i boardPosition) {
        registry.removeDuel(boardPosition);
    }

    public void removeDuelById(DuelId duelId) {
        registry.removeDuelById(duelId);
    }
}
