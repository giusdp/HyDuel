package com.giusdp.htduels.command.duel;

import com.giusdp.htduels.DuelRegistry;
import com.giusdp.htduels.DuelistContext;
import com.giusdp.htduels.component.DuelComponent;
import com.giusdp.htduels.duel.Duel;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class ForfeitCommand extends AbstractAsyncCommand {

    private final DuelRegistry registry;

    public ForfeitCommand(DuelRegistry registry) {
        super("forfeit", "Forfeit the current duel");
        this.registry = registry;
    }

    @Override
    protected @NonNull CompletableFuture<Void> executeAsync(@NonNull CommandContext ctx) {
        if (!ctx.isPlayer()) {
            ctx.sendMessage(Message.raw("Only players can use this command!"));
            return CompletableFuture.completedFuture(null);
        }

        Player player = ctx.senderAs(Player.class);
        DuelistContext duelCtx = registry.getSession(player.getPlayerRef());

        if (duelCtx == null) {
            ctx.sendMessage(Message.raw("You are not in a duel!"));
            return CompletableFuture.completedFuture(null);
        }

        World world = player.getWorld();
        if (world == null) {
            ctx.sendMessage(Message.raw("No world found!"));
            return CompletableFuture.completedFuture(null);
        }

        world.execute(() -> {
            Store<EntityStore> store = duelCtx.getDuelRef().getStore();
            DuelComponent duelComp = store.getComponent(duelCtx.getDuelRef(), DuelComponent.getComponentType());

            if (duelComp == null) {
                ctx.sendMessage(Message.raw("Duel not found!"));
                return;
            }

            Duel duel = duelComp.duel;
            duel.forfeit();
        });

        return CompletableFuture.completedFuture(null);
    }
}
