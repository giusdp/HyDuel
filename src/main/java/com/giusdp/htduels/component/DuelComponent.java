package com.giusdp.htduels.component;

import com.giusdp.htduels.CardAssetRepo;
import com.giusdp.htduels.DuelsPlugin;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.eventbus.HytaleEventBus;
import com.giusdp.htduels.duelist.Bot;
import com.giusdp.htduels.duelist.DuelPlayer;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class DuelComponent implements Component<EntityStore> {
    public Duel duel;

    public DuelComponent() {
        duel = Duel.builder()
                .eventBus(new HytaleEventBus())
                .cardRepo(new CardAssetRepo())
                .addDuelist(new DuelPlayer(), false)
                .addDuelist(new Bot(), true)
                .build();
    }

    public static ComponentType<EntityStore, DuelComponent> getComponentType() {
        return DuelsPlugin.duelComponent;
    }

    @Override
    public Component<EntityStore> clone() {
        return this;
    }
}
