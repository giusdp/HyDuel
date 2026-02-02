package com.giusdp.htduels.handlers;

import com.giusdp.htduels.DuelRegistry;
import com.giusdp.htduels.DuelistContext;
import com.giusdp.htduels.interaction.CardInteractionService;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.event.events.player.PlayerMouseButtonEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import javax.annotation.Nonnull;

public class PlayerMouseButtonHandler {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    private final DuelRegistry registry;

    public PlayerMouseButtonHandler(DuelRegistry registry) {
        this.registry = registry;
    }

    public void handleMouseClick(@Nonnull PlayerMouseButtonEvent event) {
        PlayerRef playerRef = event.getPlayerRefComponent();

        DuelistContext session = registry.getSession(playerRef);

        if (session == null) {
            return;
        }

        CardInteractionService.processClick(event, session);
    }
}
