package com.giusdp.htduels.command.duel;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;

public class DuelCommand extends AbstractCommandCollection {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public DuelCommand() {
        super("duel", "Commands to interact with duels");
        this.setPermissionGroup(GameMode.Adventure);

        this.addSubCommand(new StartCommand());
        this.addSubCommand(new DisplayHandCommand());
        this.addSubCommand(new DisplayBoardCommand());
        this.addSubCommand(new PlayCardCommand());
        this.addSubCommand(new EndTurnCommand());
    }
}
