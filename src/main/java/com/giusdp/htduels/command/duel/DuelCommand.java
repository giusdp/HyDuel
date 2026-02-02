package com.giusdp.htduels.command.duel;

import com.giusdp.htduels.DuelRegistry;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;

public class DuelCommand extends AbstractCommandCollection {
    public DuelCommand(DuelRegistry registry) {
        super("duel", "Commands to interact with duels");
        this.setPermissionGroup(GameMode.Adventure);

        this.addSubCommand(new EndTurnCommand(registry));
        this.addSubCommand(new ForfeitCommand(registry));
    }
}
