package com.giusdp.htduels.duel;

import com.giusdp.htduels.CardRepo;
import com.giusdp.htduels.duelist.Duelist;
import com.hypixel.hytale.math.vector.Vector3i;

import java.util.ArrayList;
import java.util.List;

public class DuelBuilder {
    private CardRepo cardRepo;
    private Vector3i boardPosition;
    private final List<DuelistConfig> configs = new ArrayList<>();

    private record DuelistConfig(Duelist duelist, boolean isOpponentSide) {}

    public DuelBuilder cardRepo(CardRepo cardRepo) {
        this.cardRepo = cardRepo;
        return this;
    }

    public DuelBuilder boardPosition(Vector3i boardPosition) {
        this.boardPosition = boardPosition;
        return this;
    }

    public DuelBuilder addDuelist(Duelist duelist, boolean isOpponentSide) {
        configs.add(new DuelistConfig(duelist, isOpponentSide));
        return this;
    }

    public Duel build() {
        if (cardRepo == null) {
            throw new IllegalStateException("cardRepo must be set");
        }
        for (DuelistConfig config : configs) {
            config.duelist.setOpponentSide(config.isOpponentSide);
        }

        Duel duel = new Duel(DuelId.generate(), configs.stream().map(DuelistConfig::duelist).toList(), cardRepo);
        duel.setBoardPosition(boardPosition);
        return duel;
    }
}
