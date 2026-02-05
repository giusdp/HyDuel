package com.giusdp.htduels.presentation.deck;

import com.giusdp.htduels.match.deck.DeckRules;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.asset.type.item.config.ItemStackContainerConfig;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.PageManager;
import com.hypixel.hytale.server.core.entity.entities.player.windows.ItemStackContainerWindow;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.inventory.container.ItemStackItemContainer;
import com.hypixel.hytale.server.core.inventory.container.filter.FilterActionType;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public class DeckOpenInteraction extends SimpleInstantInteraction {
    @Nonnull
    public static final BuilderCodec<DeckOpenInteraction> CODEC = BuilderCodec.builder(
                    DeckOpenInteraction.class, DeckOpenInteraction::new, SimpleInstantInteraction.CODEC
            )
            .documentation("Opens a deck container with custom validation rules.")
            .build();

    @Override
    protected void firstRun(@Nonnull InteractionType type, @Nonnull InteractionContext context, @Nonnull CooldownHandler cooldownHandler) {
        Ref<EntityStore> owningEntity = context.getOwningEntity();
        Store<EntityStore> store = owningEntity.getStore();
        Player player = store.getComponent(owningEntity, Player.getComponentType());

        if (player == null) {
            return;
        }

        PageManager pageManager = player.getPageManager();
        if (pageManager.getCustomPage() != null) {
            return;
        }

        ItemStack heldItem = context.getHeldItem();
        if (ItemStack.isEmpty(heldItem)) {
            return;
        }

        byte heldItemSlot = context.getHeldItemSlot();
        ItemContainer itemContainer = player.getInventory().getSectionById(context.getHeldItemSectionId());
        if (itemContainer == null) {
            return;
        }

        ItemStack itemStack = itemContainer.getItemStack(heldItemSlot);
        if (itemStack == null) {
            return;
        }

        ItemStackContainerConfig config = itemStack.getItem().getItemStackContainerConfig();
        ItemStackItemContainer deckContainer = ItemStackItemContainer.ensureConfiguredContainer(itemContainer, heldItemSlot, config);

        if (deckContainer == null) {
            return;
        }

        // Apply adapter that enforces rules
        DeckRules rules = new DeckRules();
        int tagIndex = config.getTagIndex();
        DeckSlotFilterAdapter filter = new DeckSlotFilterAdapter(rules, deckContainer, tagIndex, player);
        for (short i = 0; i < deckContainer.getCapacity(); i++) {
            deckContainer.setSlotFilter(FilterActionType.ADD, i, filter);
        }

        // Open container with built-in window
        pageManager.setPageWithWindows(owningEntity, store, Page.Bench, true,
                new ItemStackContainerWindow(deckContainer));
    }
}
