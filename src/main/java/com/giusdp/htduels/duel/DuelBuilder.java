package com.giusdp.htduels.duel;

import com.giusdp.htduels.CardRepo;
import com.giusdp.htduels.duel.eventbus.GameEventBus;
import com.giusdp.htduels.duelist.Duelist;

import java.util.ArrayList;
import java.util.List;

public class DuelBuilder {
    private GameEventBus eventBus;
    private CardRepo cardRepo;
    private final List<DuelistConfig> configs = new ArrayList<>();

    private record DuelistConfig(Duelist duelist, boolean isOpponentSide) {}

    public DuelBuilder eventBus(GameEventBus eventBus) {
        this.eventBus = eventBus;
        return this;
    }

    public DuelBuilder cardRepo(CardRepo cardRepo) {
        this.cardRepo = cardRepo;
        return this;
    }

    public DuelBuilder addDuelist(Duelist duelist, boolean isOpponentSide) {
        configs.add(new DuelistConfig(duelist, isOpponentSide));
        return this;
    }

    public Duel build() {
        if (eventBus == null) {
            throw new IllegalStateException("eventBus must be set");
        }
        if (cardRepo == null) {
            throw new IllegalStateException("cardRepo must be set");
        }
        if (configs.size() != 2) {
            throw new IllegalStateException("exactly 2 duelists are required, got " + configs.size());
        }

        for (DuelistConfig config : configs) {
            config.duelist.setOpponentSide(config.isOpponentSide);
        }

        return new Duel(configs.get(0).duelist, configs.get(1).duelist, eventBus, cardRepo);
    }
}
