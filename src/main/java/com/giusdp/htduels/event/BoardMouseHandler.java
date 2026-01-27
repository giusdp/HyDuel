package com.giusdp.htduels.event;

import com.giusdp.htduels.CardAssetRepo;
import com.giusdp.htduels.DuelSession;
import com.giusdp.htduels.interaction.CardInteractionService;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.event.events.player.PlayerMouseButtonEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import javax.annotation.Nonnull;

public class BoardMouseHandler {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public static void handleMouseClick(@Nonnull PlayerMouseButtonEvent event) {
        PlayerRef playerRef = event.getPlayerRefComponent();

        DuelSession session = DuelSession.get(playerRef);

        if (session == null) {
            return;
        }

        CardInteractionService.processClick(event, session);
    }
}
