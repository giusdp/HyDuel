package com.giusdp.htduels.hytale.input;

import com.giusdp.htduels.hytale.DuelManager;
import com.giusdp.htduels.hytale.DuelistSessionManager;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.event.events.player.PlayerMouseButtonEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import javax.annotation.Nonnull;

public class PlayerMouseButtonHandler {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    private final DuelManager presentationService;
    private final CardInteractionService cardInteractionService;

    public PlayerMouseButtonHandler(DuelManager presentationService, CardInteractionService cardInteractionService) {
        this.presentationService = presentationService;
        this.cardInteractionService = cardInteractionService;
    }

    public void handleMouseClick(@Nonnull PlayerMouseButtonEvent event) {
        PlayerRef playerRef = event.getPlayerRefComponent();

        DuelistSessionManager session = presentationService.getSession(playerRef);

        if (session == null) {
            return;
        }

        cardInteractionService.processClick(event, session);
    }
}
