package com.giusdp.htduels.commands;

import com.hypixel.hytale.protocol.ClientCameraView;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.protocol.packets.camera.SetServerCamera;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.entity.entities.Player;

import javax.annotation.Nonnull;

/**
 * Command to reset the player's camera to normal first-person view.
 * Useful for exiting custom camera states like the board view.
 */
public class ResetCameraCommand extends CommandBase {

    public ResetCameraCommand() {
        super("reset", "Resets your camera to normal first-person view");
        this.setPermissionGroup(GameMode.Adventure);
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        if (!ctx.isPlayer()) {
            ctx.sendMessage(Message.raw("Only players can use this command!"));
            return;
        }

        Player player = ctx.senderAs(Player.class);

        player.getPlayerRef().getPacketHandler().writeNoCache(
            new SetServerCamera(ClientCameraView.FirstPerson, false, null)
        );

        ctx.sendMessage(Message.raw("Camera reset to normal!"));
    }
}
