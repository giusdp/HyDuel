package com.giusdp.htduels.interaction;

import com.giusdp.htduels.DuelistContext;
import com.giusdp.htduels.component.BoardLayoutComponent;
import com.giusdp.htduels.component.DuelComponent;
import com.giusdp.htduels.duel.positioning.BoardLayout;
import com.giusdp.htduels.ui.BoardGameUi;
import com.hypixel.hytale.math.Vec2f;
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

public class BoardInteraction extends SimpleBlockInteraction {
    public static final BuilderCodec<BoardInteraction> CODEC = BuilderCodec
            .builder(BoardInteraction.class, BoardInteraction::new, SimpleBlockInteraction.CODEC)
            .documentation("Goes to the duel board top-down camera view").build();

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private static final double Y_OFFSET = 1.75;
    private static final float CAMERA_PITCH = (float) Math.toRadians(-90.0f);
    public static final float CARD_Y_OFFSET = 1.1f;

    public BoardInteraction() {
        super("BoardActivation");
    }

    @Override
    protected void interactWithBlock(@NonNull World world, @NonNull CommandBuffer<EntityStore> commandBuffer,
                                     @NonNull InteractionType type, @NonNull InteractionContext context, @Nullable ItemStack itemInHand,
                                     @NonNull Vector3i targetBlock, @NonNull CooldownHandler cooldownHandler) {
        Ref<EntityStore> ref = context.getEntity();
        Player player = commandBuffer.getComponent(ref, Player.getComponentType());
        PlayerRef playerRef = commandBuffer.getComponent(ref, PlayerRef.getComponentType());

        assert player != null;
        BoardGameUi boardGameUi = activateBoardUI(player, playerRef, ref);

        Rotation boardRotation = getBoardRotation(world, targetBlock);
        Position cameraPosition = calculateCameraPosition(targetBlock, boardRotation);

        float cameraYaw = (float) boardRotation.getRadians();
        float cardY = targetBlock.y + CARD_Y_OFFSET;

        assert playerRef != null;
        activateBoardCamera(playerRef, cameraPosition, boardRotation);

        // Create the duel entity first
        DuelComponent duelComp = new DuelComponent();
        BoardLayout layout = createBoardLayout(targetBlock, boardRotation);
        Ref<EntityStore> duelRef = spawnDuelEntity(commandBuffer, duelComp, layout);

        // Register the active duel session
        DuelistContext ctx = DuelistContext.registerGlobal(playerRef, duelRef, duelComp.duel.getDuelist(0), cameraPosition, cameraYaw, cardY);
        ctx.setBoardGameUi(boardGameUi);
        duelComp.duel.addContext(ctx);

        LOGGER.atInfo().log("Duel entity spawned at board position (%d, %d, %d) with rotation %s",
                targetBlock.x, targetBlock.y, targetBlock.z, boardRotation.name());
    }

    private BoardGameUi activateBoardUI(Player player, PlayerRef playerRef, Ref<EntityStore> ref) {
        BoardGameUi boardGameUi = new BoardGameUi(playerRef, CustomPageLifetime.CanDismiss);
        player.getPageManager().openCustomPage(ref, ref.getStore(), boardGameUi);
        return boardGameUi;
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

    private void activateBoardCamera(@NonNull PlayerRef playerRef,
                                     Position cameraPosition, Rotation rotation) {
        ServerCameraSettings settings = createBoardCameraSettings(cameraPosition, rotation);
        playerRef.getPacketHandler().writeNoCache(new SetServerCamera(ClientCameraView.Custom, true, settings));
    }

    private Ref<EntityStore> spawnDuelEntity(CommandBuffer<EntityStore> commandBuffer, DuelComponent duelComp,
                                             BoardLayout layout) {
        Holder<EntityStore> holder = EntityStore.REGISTRY.newHolder();

        holder.addComponent(DuelComponent.getComponentType(), duelComp);
        holder.addComponent(BoardLayoutComponent.getComponentType(), new BoardLayoutComponent(layout));
        return commandBuffer.addEntity(holder, AddReason.SPAWN);
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


    private BoardLayout createBoardLayout(Vector3i boardPosition, Rotation rotation) {
        // Calculate board center based on rotation (must match camera position)
        float originX = boardPosition.x;
        float originZ = boardPosition.z;
        switch (rotation) {
            case None -> originZ += 0.5f;
            case Ninety -> {
                originX += 0.5f;
                originZ += 1f;
            }
            case OneEighty -> {
                originX += 1f;
                originZ += 0.5f;
            }
            case TwoSeventy -> originX += 0.5f;
        }
        Vec2f origin = new Vec2f(originX, originZ);

        return new BoardLayout(
                origin,
                rotation,
                0.25f,  // playerBattlefieldDepth
                0.25f,  // opponentBattlefieldDepth
                0.4f,  // playerHandDepth
                0.4f,  // opponentHandDepth
                0.8f,   // deckOffsetX
                0.15f,  // battlefieldSpacing
                0.12f,  // handSpacing
                0.2f,   // battlefieldCardWidth
                0.15f,  // handCardWidth
                boardPosition.y + 1.2f,   // handYOffset
                boardPosition.y + 1.05f   // battlefieldYOffset
        );
    }


    @Override
    protected void simulateInteractWithBlock(@NonNull InteractionType type, @NonNull InteractionContext context,
                                             @Nullable ItemStack itemInHand, @NonNull World world, @NonNull Vector3i targetBlock) {
        // Client-side simulation not needed for this interaction
    }
}
