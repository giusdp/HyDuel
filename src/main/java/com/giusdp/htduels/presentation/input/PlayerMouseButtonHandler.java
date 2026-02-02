package com.giusdp.htduels.presentation.input;

import com.giusdp.htduels.presentation.DuelPresentationService;
import com.giusdp.htduels.presentation.DuelistSessionManager;
import com.giusdp.htduels.presentation.input.CardInteractionService;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.event.events.player.PlayerMouseButtonEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import javax.annotation.Nonnull;

public class PlayerMouseButtonHandler {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    private final DuelPresentationService presentationService;

    public PlayerMouseButtonHandler(DuelPresentationService presentationService) {
        this.presentationService = presentationService;
    }

    public void handleMouseClick(@Nonnull PlayerMouseButtonEvent event) {
        PlayerRef playerRef = event.getPlayerRefComponent();

        DuelistSessionManager session = presentationService.getSession(playerRef);

        if (session == null) {
            return;
        }

        CardInteractionService.processClick(event, session);
    }
}
