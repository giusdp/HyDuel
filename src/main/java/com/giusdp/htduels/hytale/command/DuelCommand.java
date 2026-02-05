package com.giusdp.htduels.hytale.command;

import com.giusdp.htduels.hytale.DuelManager;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;

public class DuelCommand extends AbstractCommandCollection {
    public DuelCommand(DuelManager presentationService) {
        super("duel", "Commands to interact with duels");
        this.setPermissionGroup(GameMode.Adventure);

        this.addSubCommand(new EndTurnCommand(presentationService));
        this.addSubCommand(new ForfeitCommand(presentationService));
    }
}
