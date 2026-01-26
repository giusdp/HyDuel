package com.giusdp.htduels.interaction;

import com.giusdp.htduels.component.BoardLayoutComponent;
import com.giusdp.htduels.component.DuelComponent;
import com.giusdp.htduels.duel.Card;
import com.giusdp.htduels.duel.positioning.BoardLayout;
import com.giusdp.htduels.duel.positioning.CardPositioningService;
import com.giusdp.htduels.duelist.Duelist;
import com.giusdp.htduels.spawn.CardSpawner;
import com.giusdp.htduels.ui.BoardGameUi;
import com.hypixel.hytale.math.Vec2f;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.AddReason;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.*;
import com.hypixel.hytale.protocol.packets.camera.SetServerCamera;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.Rotation;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.RotationTuple;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.SimpleBlockInteraction;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.math.util.ChunkUtil;
import org.jspecify.annotations.NonNull;

import javax.annotation.Nullable;

/**
 * Handles the interaction with the Board block. When a player presses F on the
 * board, it switches to a top-down tabletop camera view.
 */
public class BoardInteraction extends SimpleBlockInteraction {
    public static final BuilderCodec<BoardInteraction> CODEC = BuilderCodec
            .builder(BoardInteraction.class, BoardInteraction::new, SimpleBlockInteraction.CODEC)
            .documentation("Goes to the duel board top-down camera view").build();

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private static final double Y_OFFSET = 1.75;
    private static final float CAMERA_PITCH = -90.0f;

    public BoardInteraction() {
        super("BoardActivation");
    }

    public static Position cameraPos;

    @Override
    protected void interactWithBlock(@NonNull World world, @NonNull CommandBuffer<EntityStore> commandBuffer,
                                     @NonNull InteractionType type, @NonNull InteractionContext context, @Nullable ItemStack itemInHand,
                                     @NonNull Vector3i targetBlock, @NonNull CooldownHandler cooldownHandler) {
        Ref<EntityStore> ref = context.getEntity();
        Player player = commandBuffer.getComponent(ref, Player.getComponentType());
        PlayerRef playerRef = commandBuffer.getComponent(ref, PlayerRef.getComponentType());

        assert player != null;
        assert playerRef != null;
        player.getPageManager().openCustomPage(ref, ref.getStore(), new BoardGameUi(playerRef, CustomPageLifetime.CanDismiss));

        Rotation boardRotation = getBoardRotation(world, targetBlock);
        activateBoardCamera(player, playerRef, targetBlock, boardRotation);
        //spawnDuel(commandBuffer, targetBlock, boardRotation);
    }


    private Rotation getBoardRotation(@NonNull World world, @NonNull Vector3i blockPos) {
        long chunkIndex = ChunkUtil.indexChunkFromBlock(blockPos.x, blockPos.z);
        WorldChunk chunk = world.getChunkIfInMemory(chunkIndex);
        if (chunk == null) {
            return Rotation.None;
        }

        // Bitwise AND performs modulo 32
        int localX = blockPos.x & 31;
        int localZ = blockPos.z & 31;

        int rotationIndex = chunk.getRotationIndex(localX, blockPos.y, localZ);
        return RotationTuple.get(rotationIndex).yaw();
    }

    private void activateBoardCamera(@NonNull Player player, @NonNull PlayerRef playerRef,
                                     Vector3i boardPosition, Rotation rotation) {
        Position cameraPosition = calculateCameraPosition(boardPosition, rotation);
        ServerCameraSettings settings = createBoardCameraSettings(cameraPosition, rotation);

        playerRef.getPacketHandler().writeNoCache(new SetServerCamera(ClientCameraView.Custom, true, settings));

        assert settings.rotation != null;
        LOGGER.atInfo().log("Board camera activated for player %s at position (%f, %f, %f) with rotation (%f, %f, %f)", player.getDisplayName(),
                cameraPosition.x, cameraPosition.y, cameraPosition.z, settings.rotation.yaw, settings.rotation.pitch, settings.rotation.roll);
    }

    private Position calculateCameraPosition(@NonNull Vector3i boardPosition, Rotation rotation) {
        // Board is 2 blocks - offset by 0.5 in the direction it extends
        double centerX = boardPosition.x;
        double centerY = boardPosition.y + Y_OFFSET;
        double centerZ = boardPosition.z;

        switch (rotation) {
            case None -> centerZ += 0.5;
            case Ninety -> {
                centerX += 0.5;
                centerZ += 1;
            }
            case OneEighty -> {
                centerX += 1;
                centerZ += 0.5;
            }
            case TwoSeventy -> centerX += 0.5;
        }

        return new Position(centerX, centerY, centerZ);
    }

    private ServerCameraSettings createBoardCameraSettings(@NonNull Position cameraPosition, Rotation rotation) {
        ServerCameraSettings settings = new ServerCameraSettings();

        var rotDegrees = rotation.getDegrees();
        LOGGER.atInfo().log("DEBUG: Setting camera rotation to %d degrees", rotDegrees);


        // Set absolute camera position above board
        settings.positionType = PositionType.Custom;
        settings.position = cameraPosition;

        // Look straight down with rotation matching board orientation
        settings.rotationType = RotationType.Custom;

        // I honestly don't know why we need to convert to radians here but not for the pitch
        settings.rotation = new Direction((float) rotation.getRadians(), CAMERA_PITCH, 0);

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
        settings.planeNormal = new com.hypixel.hytale.protocol.Vector3f(0, 1, 0);
        return settings;
    }

    private void spawnDuel(CommandBuffer<EntityStore> commandBuffer, Vector3i boardPosition, Rotation rotation) {
        Holder<EntityStore> holder = EntityStore.REGISTRY.newHolder();
        DuelComponent duelComp = new DuelComponent();
        BoardLayout layout = createBoardLayout(boardPosition, rotation);

        holder.addComponent(DuelComponent.getComponentType(), duelComp);
        holder.addComponent(BoardLayoutComponent.getComponentType(), new BoardLayoutComponent(layout));
        Ref<EntityStore> duelRef = commandBuffer.addEntity(holder, AddReason.SPAWN);

        float boardY = boardPosition.y + 1.0f;
        spawnHandCards(commandBuffer, duelRef, duelComp.duel.duelist1, layout, boardY);
        spawnHandCards(commandBuffer, duelRef, duelComp.duel.duelist2, layout, boardY);

        LOGGER.atInfo().log("Duel entity spawned at board position (%d, %d, %d) with rotation %s",
                boardPosition.x, boardPosition.y, boardPosition.z, rotation.name());
    }

    private void spawnHandCards(CommandBuffer<EntityStore> commandBuffer, Ref<EntityStore> duelRef,
                                Duelist duelist, BoardLayout layout, float boardY) {
        for (Card card : duelist.getHand().getCards()) {
            Vec2f pos2d = CardPositioningService.getWorldPosition(card, layout);
            Vector3d pos = new Vector3d(pos2d.x, boardY, pos2d.y);
            CardSpawner.spawn(commandBuffer, duelRef, card, pos);
        }
    }

    private BoardLayout createBoardLayout(Vector3i boardPosition, Rotation rotation) {
        // Calculate board center based on rotation (must match camera position)
        float originX = boardPosition.x;
        float originZ = boardPosition.z;
        switch (rotation) {
            case None, OneEighty -> originZ += 0.5f;
            case Ninety, TwoSeventy -> originX += 0.5f;
        }
        Vec2f origin = new Vec2f(originX, originZ);

        return new BoardLayout(
                origin,
                rotation,
                0.3f,   // playerBattlefieldDepth
                0.3f,   // opponentBattlefieldDepth
                0.6f,   // playerHandDepth
                0.6f,   // opponentHandDepth
                0.8f,   // deckOffsetX
                0.3f,   // battlefieldSpacing
                0.12f,  // handSpacing
                0.2f,   // battlefieldCardWidth
                0.15f   // handCardWidth
        );
    }




    @Override
    protected void simulateInteractWithBlock(@NonNull InteractionType type, @NonNull InteractionContext context,
                                             @Nullable ItemStack itemInHand, @NonNull World world, @NonNull Vector3i targetBlock) {
        // Client-side simulation not needed for this interaction
    }
}
