package com.giusdp.htduels;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.MouseButtonState;
import com.hypixel.hytale.protocol.MouseButtonType;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
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

        MouseButtonType buttonType = event.getMouseButton().mouseButtonType;
        Vector3i targetBlock = event.getTargetBlock();

        if (targetBlock == null) {
            LOGGER.atInfo().log("Player clicked but no block targeted");
            return;
        }

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
