package com.giusdp.htduels.hytale.input;

import com.giusdp.htduels.hytale.DuelManager;
import com.giusdp.htduels.hytale.deck.DeckContainerUtils;
import com.giusdp.htduels.match.DuelRegistry;
import com.giusdp.htduels.match.deck.DeckRules;
import com.giusdp.htduels.hytale.ecs.component.DuelComponent;
import com.giusdp.htduels.match.Duel;
import com.giusdp.htduels.match.phases.WaitingPhase;
import com.giusdp.htduels.hytale.ui.DuelModeSelectionPage;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.Rotation;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.RotationTuple;
import com.hypixel.hytale.server.core.asset.type.item.config.ItemStackContainerConfig;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.inventory.container.ItemStackItemContainer;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.SimpleBlockInteraction;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.NonNull;

import javax.annotation.Nullable;
import java.util.List;

public class BoardInteraction extends SimpleBlockInteraction {

    private final DuelManager presentationService;
    private final DuelRegistry registry;

    public BoardInteraction(DuelManager presentationService, DuelRegistry registry) {
        super("BoardActivation");
        this.presentationService = presentationService;
        this.registry = registry;
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

        Ref<EntityStore> existingDuel = presentationService.findDuelAt(targetBlock);
        if (existingDuel == null) {
            if (!isValidDeck(player, itemInHand, context)) {
                return;
            }
            DuelModeSelectionPage page = new DuelModeSelectionPage(playerRef, boardContext, presentationService);
            player.getPageManager().openCustomPage(ref, ref.getStore(), page);
            return;
        }

        DuelComponent duelComp = commandBuffer.getComponent(existingDuel, DuelComponent.getComponentType());
        if (duelComp == null) {
            return;
        }

        Duel duel = registry.findDuel(duelComp.getDuelId());
        if (duel != null && duel.isInPhase(WaitingPhase.class)) {
            presentationService.joinAsPlayer(playerRef, boardContext, ref.getStore(), existingDuel);
        }
    }

    private boolean isValidDeck(Player player, ItemStack itemInHand, InteractionContext context) {
        if (ItemStack.isEmpty(itemInHand)) {
            player.sendMessage(Message.raw("You need to hold a deck to start a duel."));
            return false;
        }

        if (!"Deck".equals(itemInHand.getItemId())) {
            player.sendMessage(Message.raw("You need to hold a deck to start a duel."));
            return false;
        }

        ItemStackContainerConfig config = itemInHand.getItem().getItemStackContainerConfig();

        ItemContainer heldItemContainer = player.getInventory().getSectionById(context.getHeldItemSectionId());
        if (heldItemContainer == null) {
            return false;
        }

        ItemStackItemContainer deckContainer = ItemStackItemContainer.ensureConfiguredContainer(
                heldItemContainer, context.getHeldItemSlot(), config);
        if (deckContainer == null) {
            return false;
        }

        List<String> cardIds = DeckContainerUtils.extractCardIds(deckContainer);
        DeckRules rules = new DeckRules();
        if (!rules.isValidForDuel(cardIds.size())) {
            int needed = rules.cardsNeeded(cardIds.size());
            player.sendMessage(Message.raw("Your deck needs " + needed + " more cards (20 required)."));
            return false;
        }

        return true;
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
