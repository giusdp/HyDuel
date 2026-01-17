package com.giusdp.htduels;

import com.giusdp.htduels.components.CardComponent;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.MouseButtonState;
import com.hypixel.hytale.protocol.MouseButtonType;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.entity.Entity;
import com.hypixel.hytale.server.core.event.events.player.PlayerMouseButtonEvent;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

/**
 * Handles mouse click events when viewing the board.
 * Logs what was clicked for now.
 */
public class BoardMouseHandler {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public static void handleMouseClick(@Nonnull PlayerMouseButtonEvent event) {
        // Only handle when mouse button is released (not pressed)
        if (event.getMouseButton().state != MouseButtonState.Released) {
            return;
        }

        LOGGER.atInfo().log("Mouse click event received - button state: Released");

        MouseButtonType buttonType = event.getMouseButton().mouseButtonType;

        // Check for entity clicks first
        Entity targetEntity = event.getTargetEntity();
        LOGGER.atInfo().log("Target entity: %s", targetEntity);

        if (targetEntity != null) {
            handleEntityClick(event, buttonType, targetEntity);
            return;
        }

        // Then check for block clicks
        Vector3i targetBlock = event.getTargetBlock();
        LOGGER.atInfo().log("Target block: %s", targetBlock);

        if (targetBlock != null) {
            handleBlockClick(event, buttonType, targetBlock);
            return;
        }

        LOGGER.atInfo().log("Player clicked but nothing was targeted");
    }

    private static void handleEntityClick(@Nonnull PlayerMouseButtonEvent event,
                                          MouseButtonType buttonType,
                                          Entity targetEntity) {
        // Get the entity reference
        Ref<EntityStore> entityRef = targetEntity.getReference();
        if (entityRef == null) {
            LOGGER.atInfo().log("Entity has no reference");
            return;
        }

        // Get the world and store
        World world = event.getPlayerRef().getStore().getExternalData().getWorld();
        Store<EntityStore> store = world.getEntityStore().getStore();

        // Check if it's a card entity
        CardComponent cardComponent = store.getComponent(entityRef, CardComponent.getComponentType());

        if (cardComponent != null) {
            LOGGER.atInfo().log(
                    "Player %s clicked with %s on card: %s",
                    event.getPlayer().getDisplayName(),
                    buttonType,
                    cardComponent.name
            );

            event.getPlayer().sendMessage(Message.raw(
                    String.format("Clicked %s on card: %s", buttonType, cardComponent.name)
            ));
        } else {
            LOGGER.atInfo().log(
                    "Player %s clicked with %s on unknown entity",
                    event.getPlayer().getDisplayName(),
                    buttonType
            );

            event.getPlayer().sendMessage(Message.raw(
                    String.format("Clicked %s on entity", buttonType)
            ));
        }
    }

    private static void handleBlockClick(@Nonnull PlayerMouseButtonEvent event,
                                         MouseButtonType buttonType,
                                         Vector3i targetBlock) {
        // Get the world and block information
        World world = event.getPlayerRef().getStore().getExternalData().getWorld();
        BlockType blockType = world.getBlockType(targetBlock);

        String blockName = "Air";
        if (blockType != null) {
            blockName = blockType.getItem().getId();
        }

        LOGGER.atInfo().log(
                "Player %s clicked with %s at block position (%d, %d, %d) - Block: %s",
                event.getPlayer().getDisplayName(),
                buttonType,
                targetBlock.x,
                targetBlock.y,
                targetBlock.z,
                blockName
        );

        // Send feedback to player
        event.getPlayer().sendMessage(Message.raw(
                String.format("Clicked %s at (%d, %d, %d) - %s",
                        buttonType, targetBlock.x, targetBlock.y, targetBlock.z, blockName)
        ));
    }
}
