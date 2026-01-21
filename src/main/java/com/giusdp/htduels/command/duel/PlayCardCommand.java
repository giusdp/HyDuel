package com.giusdp.htduels.command.duel;

import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.component.DuelComponent;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.event.PlayCard;
import com.giusdp.htduels.duelist.Duelist;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PlayCardCommand extends AbstractAsyncCommand {

    private final RequiredArg<Integer> cardIndex;

    public PlayCardCommand() {
        super("play", "Shows player's hand");
        cardIndex = withRequiredArg("cardIndex", "The index of the card to play from the hand", ArgTypes.INTEGER);
    }

    @Override
    protected @NonNull CompletableFuture<Void> executeAsync(@NonNull CommandContext ctx) {
        DuelComponent duelComponent = DuelComponent.getActiveDuel();
        if (duelComponent == null) {
            ctx.sendMessage(Message.raw("No active duel found! Use /duel start first."));
            return CompletableFuture.completedFuture(null);
        }

        Duel duel = duelComponent.duel;
        var card = duel.activeDuelist.getHand().get(cardIndex.get(ctx));
        duel.emit(new PlayCard(duel, duel.activeDuelist, card));

        ctx.sendMessage(Message.raw("Played card: " + card.name()));
        return CompletableFuture.completedFuture(null);
    }


}
