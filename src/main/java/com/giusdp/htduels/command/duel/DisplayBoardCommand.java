package com.giusdp.htduels.command.duel;

import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.component.DuelComponent;
import com.giusdp.htduels.duel.Card;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.zone.Battlefield;
import com.giusdp.htduels.duelist.Duelist;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DisplayBoardCommand extends AbstractAsyncCommand {

    public DisplayBoardCommand() {
        super("board", "Shows cards on the board");
    }

    public static void formatCardList(List<Card> cards, StringBuilder sb) {
        for (int i = 0; i < cards.size(); i++) {
            CardAsset card = cards.get(i).getAsset();
            sb.append("[").append(i).append("] ")
                    .append(card.name())
                    .append(" (Cost: ").append(card.cost())
                    .append(", ATK: ").append(card.attack())
                    .append(", HP: ").append(card.health())
                    .append(", ").append(card.type())
                    .append(")\n");
        }
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

        Duelist duelist1 = duel.duelist1;
        Duelist duelist2 = duel.duelist2;
        Battlefield side1 = duelist1.getBattlefield();
        Battlefield side2 = duelist2.getBattlefield();

        formatCardList(side1.getCards(), sb);
        sb.append("-".repeat(22)).append("\n");

        formatCardList(side2.getCards(), sb);

        sb.append("=".repeat(22));
        return sb.toString();
    }
}
