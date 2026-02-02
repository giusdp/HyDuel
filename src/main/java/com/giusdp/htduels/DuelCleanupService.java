package com.giusdp.htduels;

import com.giusdp.htduels.component.DuelComponent;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.interaction.CardInteractionService;
import com.hypixel.hytale.math.vector.Vector3i;
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

public final class DuelCleanupService {

    private final DuelRegistry registry;

    public DuelCleanupService(DuelRegistry registry) {
        this.registry = registry;
    }

    public void cleanup(Ref<EntityStore> duelRef, DuelComponent duelComp, Duel duel, CommandBuffer<EntityStore> commandBuffer) {
        // Remove all card entities from the duel component
        for (Ref<EntityStore> cardRef : duelComp.getCardEntities()) {
            commandBuffer.removeEntity(cardRef, RemoveReason.REMOVE);
        }

        // Player-specific cleanup per context
        for (DuelistContext ctx : duel.getContexts()) {
            PlayerRef playerRef = ctx.getPlayerRef();
            if (playerRef == null) {
                continue;
            }

            // Dismiss the board game UI
            dismissBoardGameUi(playerRef);

            // Reset camera to first person
            resetCamera(playerRef);

            // Clear hover state
            CardInteractionService.clearHoveredCard(playerRef);

        }

        // Remove from active duels registry
        Vector3i boardPosition = duel.getBoardPosition();
        if (boardPosition != null) {
            registry.removeDuel(boardPosition);
        }

        // Remove from duel ID registry
        registry.removeDuelById(duelComp.getDuelId());

        // Remove the duel entity itself
        commandBuffer.removeEntity(duelRef, RemoveReason.REMOVE);

        // Unregister all player contexts for this duel
        registry.unregisterByDuelRef(duelRef);
    }

    private static void resetCamera(PlayerRef playerRef) {
        SetServerCamera serverCamera = new SetServerCamera(ClientCameraView.FirstPerson, false, null);
        playerRef.getPacketHandler().writeNoCache(serverCamera);
    }

    private static void dismissBoardGameUi(PlayerRef playerRef) {
        Ref<EntityStore> playerEntityRef = playerRef.getReference();
        if (playerEntityRef == null) {
            return;
        }

        Store<EntityStore> store = playerEntityRef.getStore();
        Player player = store.getComponent(playerEntityRef, Player.getComponentType());
        if (player != null) {
            player.getPageManager().setPage(playerEntityRef, store, Page.None);
        }
    }
}
