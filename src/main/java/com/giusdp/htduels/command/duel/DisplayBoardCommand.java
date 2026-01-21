package com.giusdp.htduels.command.duel;

import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.component.DuelComponent;
import com.giusdp.htduels.duel.Battlefield;
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

public class DisplayBoardCommand extends AbstractAsyncCommand {

    public DisplayBoardCommand() {
        super("board", "Shows cards on the board");
    }

    @Override
    protected @NonNull CompletableFuture<Void> executeAsync(@NonNull CommandContext ctx) {
        DuelComponent duelComponent = DuelComponent.getActiveDuel();
        if (duelComponent == null) {
            ctx.sendMessage(Message.raw("No active duel found! Use /duel start first."));
            return CompletableFuture.completedFuture(null);
        }

        Duel duel = duelComponent.duel;


        ctx.sendMessage(Message.raw(formatBoard(duel)));
        return CompletableFuture.completedFuture(null);
    }

    String formatBoard(Duel duel) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== BOARD ===\n");

        Battlefield battlefield = duel.battlefield;
        Duelist duelist1 = duel.duelist1;
        Duelist duelist2 = duel.duelist2;

        var side1 = battlefield.getSide(duelist1);
        var side2 = battlefield.getSide(duelist2);

        formatCardList(side1, sb);
        sb.append("-".repeat(22)).append("\n");

        formatCardList(side2, sb);

        sb.append("=".repeat(22));
        return sb.toString();
    }


    public static void formatCardList(List<CardAsset> cards, StringBuilder sb) {
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
    }
}
