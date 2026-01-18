package com.giusdp.htduels.interactions;

import com.giusdp.htduels.ui.BoadGameUI;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.*;
import com.hypixel.hytale.protocol.MouseInputType;
import com.hypixel.hytale.protocol.packets.camera.SetServerCamera;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.SimpleBlockInteraction;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.NonNull;

import javax.annotation.Nullable;

/**
 * Handles the interaction with the Board block.
 * When a player presses F on the board, it switches to a top-down tabletop camera view.
 */
public class BoardInteraction extends SimpleBlockInteraction {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    private static final double X_OFFSET = 0.0;   // Center of 1-block width
    private static final double Y_OFFSET = 1.75;
    private static final double Z_OFFSET = 0.5;   // Center of 2-block depth
    private static final float CAMERA_YAW = 0.0f; // Change this to 90, 180, or 270 to rotate the view
    private static final float CAMERA_PITCH = -90.0f;

    public static final BuilderCodec<BoardInteraction> CODEC =
            BuilderCodec.builder(BoardInteraction.class, BoardInteraction::new, SimpleBlockInteraction.CODEC)
                    .documentation("Opens the game board interaction with a top-down camera view")
                    .build();

    public BoardInteraction() {
        super("BoardActivation");
    }

    @Override
    protected void interactWithBlock(
            @NonNull World world,
            @NonNull CommandBuffer<EntityStore> commandBuffer,
            @NonNull InteractionType type,
            @NonNull InteractionContext context,
            @Nullable ItemStack itemInHand,
            @NonNull Vector3i targetBlock,
            @NonNull CooldownHandler cooldownHandler
    ) {
        Ref<EntityStore> ref = context.getEntity();
        Player player = commandBuffer.getComponent(ref, Player.getComponentType());

        player.getPageManager().openCustomPage(ref, ref.getStore(), new BoadGameUI( player.getPlayerRef(), CustomPageLifetime.CanDismiss));

        assert player != null;
        activateBoardCamera(player, targetBlock);
    }

    /**
     * Activates the board camera view for the player.
     * Sets up a fixed top-down camera position above the board.
     */
    private void activateBoardCamera(@NonNull Player player, @NonNull Vector3i boardPosition) {
        PlayerRef playerRef = player.getPlayerRef();

        Position cameraPosition = calculateCameraPosition(boardPosition);
        ServerCameraSettings settings = createBoardCameraSettings(cameraPosition);

        playerRef.getPacketHandler().writeNoCache(
            new SetServerCamera(ClientCameraView.Custom, false, settings)
        );

        LOGGER.atInfo().log(
            "Board camera activated for player %s at position (%f, %f, %f)",
            player.getDisplayName(),
            cameraPosition.x,
            cameraPosition.y,
            cameraPosition.z
        );
    }

    private Position calculateCameraPosition(@NonNull Vector3i boardPosition) {
        double centerX = boardPosition.x + X_OFFSET;
        double centerY = boardPosition.y + Y_OFFSET;
        double centerZ = boardPosition.z + Z_OFFSET;
        return new Position(centerX, centerY, centerZ);
    }

    private ServerCameraSettings createBoardCameraSettings(@NonNull Position cameraPosition) {
        ServerCameraSettings settings = new ServerCameraSettings();

        // Set absolute camera position above board
        settings.positionType = PositionType.Custom;
        settings.position = cameraPosition;

        // Look straight down with fixed orientation
        settings.rotationType = RotationType.Custom;
        settings.rotation = new Direction(CAMERA_YAW, CAMERA_PITCH, 0);

        // Freeze player movement
        settings.movementMultiplier = new com.hypixel.hytale.protocol.Vector3f(0, 0, 0);
        settings.applyMovementType = ApplyMovementType.Position;

        // Configure UI elements
        settings.displayCursor = true;
        settings.displayReticle = false;
        settings.skipCharacterPhysics = true;

        // Enable mouse input for clicking on cards
        settings.sendMouseMotion = true;
        settings.mouseInputType = MouseInputType.LookAtTargetEntity; // Only detect card entities
        settings.planeNormal = new com.hypixel.hytale.protocol.Vector3f(0, 1, 0); // Y-up plane (table surface)

        return settings;
    }

    @Override
    protected void simulateInteractWithBlock(
            @NonNull InteractionType type,
            @NonNull InteractionContext context,
            @Nullable ItemStack itemInHand,
            @NonNull World world,
            @NonNull Vector3i targetBlock
    ) {
        // Client-side simulation not needed for this interaction
    }
}
