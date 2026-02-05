package com.giusdp.htduels.hytale.command;

import com.giusdp.htduels.hytale.DuelManager;
import com.giusdp.htduels.hytale.DuelistSessionManager;
import com.giusdp.htduels.hytale.ecs.component.DuelComponent;
import com.giusdp.htduels.match.Duel;
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

    private final DuelManager presentationService;

    public ForfeitCommand(DuelManager presentationService) {
        super("forfeit", "Forfeit the current duel");
        this.presentationService = presentationService;
    }

    @Override
    protected @NonNull CompletableFuture<Void> executeAsync(@NonNull CommandContext ctx) {
        if (!ctx.isPlayer()) {
            ctx.sendMessage(Message.raw("Only players can use this command!"));
            return CompletableFuture.completedFuture(null);
        }

        Player player = ctx.senderAs(Player.class);
        DuelistSessionManager duelCtx = presentationService.getSession(player.getPlayerRef());

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

            Duel duel = duelCtx.getDuel();
            duel.forfeit();
        });

        return CompletableFuture.completedFuture(null);
    }
}
