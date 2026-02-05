package com.giusdp.htduels.hytale.deck;

import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;

import java.util.ArrayList;
import java.util.List;

public final class DeckContainerUtils {

    private DeckContainerUtils() {}

    public static List<String> extractCardIds(ItemContainer container) {
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
