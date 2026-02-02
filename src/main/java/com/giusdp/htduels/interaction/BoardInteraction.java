package com.giusdp.htduels.interaction;

import com.giusdp.htduels.component.DuelComponent;
import com.giusdp.htduels.duel.phases.WaitingPhase;
import com.giusdp.htduels.ui.DuelModeSelectionPage;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.InteractionType;
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
import org.jspecify.annotations.NonNull;

import javax.annotation.Nullable;

public class BoardInteraction extends SimpleBlockInteraction {
    public static final BuilderCodec<BoardInteraction> CODEC = BuilderCodec
            .builder(BoardInteraction.class, BoardInteraction::new, SimpleBlockInteraction.CODEC)
            .documentation("Goes to the duel board top-down camera view").build();

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

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
        assert playerRef != null;

        Rotation boardRotation = getBoardRotation(world, targetBlock);
        BoardContext boardContext = new BoardContext(targetBlock, boardRotation, ref);

        Ref<EntityStore> existingDuel = DuelSetupService.findDuelAt(targetBlock);
        if (existingDuel == null) {
            DuelModeSelectionPage page = new DuelModeSelectionPage(playerRef, boardContext);
            player.getPageManager().openCustomPage(ref, ref.getStore(), page);
            return;
        }

        // otherwise if duel already exists on this board
        DuelComponent duelComp = commandBuffer.getComponent(existingDuel, DuelComponent.getComponentType());
        if (duelComp != null && duelComp.duel.isInPhase(WaitingPhase.class)) {
            DuelSetupService.joinAsPlayer(playerRef, boardContext, ref.getStore(), existingDuel);
        }
    }

    private Rotation getBoardRotation(@NonNull World world, @NonNull Vector3i blockPos) {
        long chunkIndex = ChunkUtil.indexChunkFromBlock(blockPos.x, blockPos.z);
        WorldChunk chunk = world.getChunkIfInMemory(chunkIndex);
        if (chunk == null) {
            return Rotation.None;
        }

        int localX = blockPos.x & 31;
        int localZ = blockPos.z & 31;

        int rotationIndex = chunk.getRotationIndex(localX, blockPos.y, localZ);
        return RotationTuple.get(rotationIndex).yaw();
    }

    @Override
    protected void simulateInteractWithBlock(@NonNull InteractionType type, @NonNull InteractionContext context,
                                             @Nullable ItemStack itemInHand, @NonNull World world, @NonNull Vector3i targetBlock) {
    }
}
