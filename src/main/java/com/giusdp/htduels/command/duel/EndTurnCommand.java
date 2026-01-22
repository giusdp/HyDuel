package com.giusdp.htduels.command.duel;

import com.giusdp.htduels.component.DuelComponent;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.event.EndMainPhase;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class EndTurnCommand extends AbstractAsyncCommand {

    public EndTurnCommand() {
        super("endturn", "End the Main phase");
    }

    @Override
    protected @NonNull CompletableFuture<Void> executeAsync(@NonNull CommandContext ctx) {
        DuelComponent duelComponent = DuelComponent.getActiveDuel();
        if (duelComponent == null) {
            ctx.sendMessage(Message.raw("No active duel found! Use /duel start first."));
            return CompletableFuture.completedFuture(null);
        }

        Duel duel = duelComponent.duel;
        duel.emit(new EndMainPhase(duel));
        ctx.sendMessage(Message.raw("Turn done."));
        return CompletableFuture.completedFuture(null);
    }
}
