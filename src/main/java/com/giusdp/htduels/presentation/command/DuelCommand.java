package com.giusdp.htduels.presentation.command;

import com.giusdp.htduels.presentation.DuelPresentationService;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;

public class DuelCommand extends AbstractCommandCollection {
    public DuelCommand(DuelPresentationService presentationService) {
        super("duel", "Commands to interact with duels");
        this.setPermissionGroup(GameMode.Adventure);

        this.addSubCommand(new EndTurnCommand(presentationService));
        this.addSubCommand(new ForfeitCommand(presentationService));
    }
}
