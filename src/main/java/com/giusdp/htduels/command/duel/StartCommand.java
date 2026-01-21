package com.giusdp.htduels.command.duel;

import com.giusdp.htduels.component.DuelComponent;
import com.hypixel.hytale.component.AddReason;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.tracker.NetworkId;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class StartCommand extends AbstractAsyncCommand {
    public StartCommand() {
        super("start", "Starts a duel by spawning a duel entity");
    }

    @Override
    protected @NonNull CompletableFuture<Void> executeAsync(@NonNull CommandContext ctx) {
        if (!ctx.isPlayer()) {
            ctx.sendMessage(Message.raw("Only players can use this command!"));
            return CompletableFuture.completedFuture(null);
        }

        Player player = ctx.senderAs(Player.class);
        World world = player.getWorld();

        if (world == null) {
            ctx.sendMessage(Message.raw("Needs a world!"));
            return CompletableFuture.completedFuture(null);
        }

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
        return CompletableFuture.completedFuture(null);
    }
}
