package com.giusdp.htduels.component;

import com.giusdp.htduels.CardAssetRepo;
import com.giusdp.htduels.DuelsPlugin;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.eventbus.HytaleEventBus;
import com.giusdp.htduels.duelist.Bot;
import com.giusdp.htduels.duelist.DuelPlayer;
import com.giusdp.htduels.duelist.Duelist;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class DuelComponent implements Component<EntityStore> {
    public Duel duel;

    public DuelComponent() {
        Duelist player = new DuelPlayer();
        Duelist bot = new Bot();
        player.setBottomPlayer(true);
        bot.setBottomPlayer(false);
        duel = new Duel(player, bot, new HytaleEventBus(), new CardAssetRepo());

        duel.setup();
    }

    public static ComponentType<EntityStore, DuelComponent> getComponentType() {
        return DuelsPlugin.duelComponent;
    }

    @Override
    public Component<EntityStore> clone() {
        return this;
    }
}
