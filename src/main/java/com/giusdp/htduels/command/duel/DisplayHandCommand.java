package com.giusdp.htduels.command.duel;

import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.component.DuelComponent;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duelist.Duelist;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DisplayHandCommand extends AbstractAsyncCommand {

    private final RequiredArg<Integer> duelistIndex;

    public DisplayHandCommand() {
        super("hand", "Shows player's hand");
        duelistIndex = withRequiredArg("duelistIndex", "The index of the duelist (0 or 1)", ArgTypes.INTEGER);
    }

    @Override
    protected @NonNull CompletableFuture<Void> executeAsync(@NonNull CommandContext ctx) {
        DuelComponent duelComponent = DuelComponent.getActiveDuel();
        if (duelComponent == null) {
            ctx.sendMessage(Message.raw("No active duel found! Use /duel start first."));
            return CompletableFuture.completedFuture(null);
        }

        Duel duel = duelComponent.duel;
        Duelist duelist = null;
        if (duelistIndex.get(ctx) == 0) {
            duelist = duel.duelist1;
        } else {
            duelist = duel.duelist2;
        }

        ctx.sendMessage(Message.raw(formatHand(duelistIndex.get(ctx), duelist)));
        return CompletableFuture.completedFuture(null);
    }

    String formatHand(int index, Duelist duelist) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== PLAYER ").append(index).append(" HAND ===\n");

        List<CardAsset> cards = duelist.getHand();
        for (int i = 0; i < cards.size(); i++) {
            CardAsset card = cards.get(i);
            sb.append("[").append(i).append("] ")
                    .append(card.name())
                    .append(" (Cost: ").append(card.cost())
                    .append(", ATK: ").append(card.attack())
                    .append(", HP: ").append(card.health())
                    .append(", ").append(card.type())
                    .append(")\n");
        }

        sb.append("=".repeat(22 + String.valueOf(index).length()));
        return sb.toString();
    }
}
