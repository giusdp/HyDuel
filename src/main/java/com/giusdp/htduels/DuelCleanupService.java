package com.giusdp.htduels;

import com.giusdp.htduels.component.DuelComponent;
import com.giusdp.htduels.interaction.CardInteractionService;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.RemoveReason;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.ClientCameraView;
import com.hypixel.hytale.protocol.packets.camera.SetServerCamera;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.List;

public final class DuelCleanupService {

    private DuelCleanupService() {
    }

    public static void cleanup(Ref<EntityStore> duelRef, DuelComponent duelComp, CommandBuffer<EntityStore> commandBuffer) {
        // Remove all card entities from the duel
        for (Ref<EntityStore> cardRef : duelComp.duel.getCardEntities()) {
            commandBuffer.removeEntity(cardRef, RemoveReason.REMOVE);
        }

        // Player-specific cleanup per context
        for (DuelistContext ctx : duelComp.duel.getContexts()) {
            PlayerRef playerRef = ctx.getPlayerRef();
            if (playerRef != null) {
                // Dismiss the board game UI
                Ref<EntityStore> playerEntityRef = playerRef.getReference();
                if (playerEntityRef != null) {
                    Store<EntityStore> store = playerEntityRef.getStore();
                    Player player = store.getComponent(playerEntityRef, Player.getComponentType());
                    if (player != null) {
                        player.getPageManager().setPage(playerEntityRef, store, Page.None);
                    }
                }

                // Reset camera to first person
                playerRef.getPacketHandler()
                        .writeNoCache(new SetServerCamera(ClientCameraView.FirstPerson, false, null));

                // Clear hover state
                CardInteractionService.clearHoveredCard(playerRef);
            }
        }

        // Remove the duel entity itself
        commandBuffer.removeEntity(duelRef, RemoveReason.REMOVE);

        // Unregister all player contexts for this duel
        DuelistContext.unregisterByDuelRef(duelRef);
    }
}
