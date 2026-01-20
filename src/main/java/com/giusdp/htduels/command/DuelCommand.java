package com.giusdp.htduels.command;

import com.giusdp.htduels.component.DuelComponent;
import com.hypixel.hytale.component.AddReason;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.tracker.NetworkId;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public class DuelCommand extends CommandBase {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public DuelCommand() {
        super("duelstart", "Spawns a test card in front of you");
        this.setPermissionGroup(GameMode.Adventure);
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        if (!ctx.isPlayer()) {
            ctx.sendMessage(Message.raw("Only players can use this command!"));
            return;
        }

        Player player = ctx.senderAs(Player.class);
        World world = player.getWorld();

        assert world != null;

        // Spawn card in front of player
        world.execute(() -> {
            EntityStore entityStore = world.getEntityStore();
            Store<EntityStore> store = entityStore.getStore();

            Holder<EntityStore> holder = EntityStore.REGISTRY.newHolder();
            holder.putComponent(NetworkId.getComponentType(), new NetworkId(store.getExternalData().takeNextNetworkId()));
            holder.addComponent(DuelComponent.getComponentType(), new DuelComponent());

            holder.ensureComponent(UUIDComponent.getComponentType());

            store.addEntity(holder, AddReason.SPAWN);

            LOGGER.atInfo().log("Duel entity spawned!");
        });

        ctx.sendMessage(Message.raw("Duel spawned!"));
    }
}
