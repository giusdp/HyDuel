package com.giusdp.htduels.presentation.input;

import com.giusdp.htduels.presentation.DuelPresentationService;
import com.giusdp.htduels.presentation.DuelistSessionManager;
import com.giusdp.htduels.presentation.input.CardInteractionService;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.event.events.player.PlayerMouseMotionEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public class PlayerMouseMotionHandler {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    private final DuelPresentationService presentationService;
    private final CardInteractionService cardInteractionService;

    public PlayerMouseMotionHandler(DuelPresentationService presentationService, CardInteractionService cardInteractionService) {
        this.presentationService = presentationService;
        this.cardInteractionService = cardInteractionService;
    }

    public void handleMouseMotion(@Nonnull PlayerMouseMotionEvent event) {
        Ref<EntityStore> playerEntityRef = event.getPlayerRef();
        Store<EntityStore> store = playerEntityRef.getStore();
        PlayerRef playerRef = store.getComponent(playerEntityRef, PlayerRef.getComponentType());

        if (playerRef == null) {
            return;
        }

        DuelistSessionManager session = presentationService.getSession(playerRef);
        if (session == null) {
            return;
        }

        cardInteractionService.processMotion(event, session);
    }
}
