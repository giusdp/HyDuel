package com.giusdp.htduels.handlers;

import com.giusdp.htduels.DuelSession;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.event.events.player.PlayerMouseMotionEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public class PlayerMouseMotionHandler {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public static void handleMouseMotion(@Nonnull PlayerMouseMotionEvent event) {
        // Get PlayerRef from the entity store using the player entity reference
        Ref<EntityStore> playerEntityRef = event.getPlayerRef();
        Store<EntityStore> store = playerEntityRef.getStore();
        PlayerRef playerRef = store.getComponent(playerEntityRef, PlayerRef.getComponentType());

        if (playerRef == null) {
            return;
        }

        DuelSession session = DuelSession.get(playerRef);
        if (session == null) {
            return;
        }

//        CardInteractionService.processMotion(event, session);
    }
}
