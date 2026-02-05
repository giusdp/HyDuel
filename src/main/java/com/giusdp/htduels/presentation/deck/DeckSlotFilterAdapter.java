package com.giusdp.htduels.presentation.deck;

import com.giusdp.htduels.match.deck.DeckRules;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.inventory.container.filter.FilterActionType;
import com.hypixel.hytale.server.core.inventory.container.filter.SlotFilter;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DeckSlotFilterAdapter implements SlotFilter {
    private final DeckRules rules;
    private final ItemContainer container;
    private final int tagIndex;
    private final Player player;

    public DeckSlotFilterAdapter(DeckRules rules, ItemContainer container, int tagIndex, Player player) {
        this.rules = rules;
        this.container = container;
        this.tagIndex = tagIndex;
        this.player = player;
    }

    @Override
    public boolean test(FilterActionType action, ItemContainer c, short slot, @Nullable ItemStack item) {
        if (action != FilterActionType.ADD || item == null) {
            return true;
        }

        // Check tag filter first (Card tag)
        if (tagIndex != Integer.MIN_VALUE) {
            if (!item.getItem().getData().getExpandedTagIndexes().contains(tagIndex)) {
                return false;
            }
        }

        // Then check 2-copy rule
        List<String> currentIds = extractCurrentCardIds();
        boolean canAdd = rules.canAddCard(currentIds, item.getItemId());
        if (!canAdd) {
            player.sendMessage(Message.raw("You already have 2 copies of this card in your deck."));
        }
        return canAdd;
    }

    private List<String> extractCurrentCardIds() {
        List<String> ids = new ArrayList<>();
        for (short i = 0; i < container.getCapacity(); i++) {
            ItemStack s = container.getItemStack(i);
            if (s != null && !ItemStack.isEmpty(s)) {
                ids.add(s.getItemId());
            }
        }
        return ids;
    }
}
