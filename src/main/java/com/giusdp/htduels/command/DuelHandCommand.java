package com.giusdp.htduels.command;

import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.component.DuelComponent;
import com.giusdp.htduels.duel.Duel;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;

import javax.annotation.Nonnull;
import java.util.List;

public class DuelHandCommand extends CommandBase {

    private final int playerIndex;

    public DuelHandCommand(int playerIndex) {
        super("duelhand" + playerIndex, "Shows player " + playerIndex + "'s hand");
        this.playerIndex = playerIndex;
        this.setPermissionGroup(GameMode.Adventure);
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        DuelComponent duelComponent = DuelComponent.getActiveDuel();
        if (duelComponent == null) {
            ctx.sendMessage(Message.raw("No active duel found! Use /duelstart first."));
            return;
        }

        Duel duel = duelComponent.duel;
//        Hand hand = duel.playerHands[playerIndex];
//
//        ctx.sendMessage(Message.raw(formatHand(playerIndex, hand)));
    }
//
//    // Package-private for testing
//    String formatHand(int playerIndex, Hand hand) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("=== PLAYER ").append(playerIndex).append(" HAND ===\n");
//
//        List<CardAsset> cards = hand.cards;
//        for (int i = 0; i < cards.size(); i++) {
//            CardAsset card = cards.get(i);
//            sb.append("[").append(i).append("] ")
//              .append(card.name())
//              .append(" (Cost: ").append(card.cost())
//              .append(", ATK: ").append(card.attack())
//              .append(", HP: ").append(card.health())
//              .append(", ").append(card.type())
//              .append(")\n");
//        }
//
//        sb.append("=".repeat(22 + String.valueOf(playerIndex).length()));
//        return sb.toString();
//    }
}
