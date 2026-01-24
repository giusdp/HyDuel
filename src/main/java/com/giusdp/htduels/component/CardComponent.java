package com.giusdp.htduels.component;

import com.giusdp.htduels.DuelsPlugin;
import com.giusdp.htduels.duel.Card;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.Nullable;

public class CardComponent implements Component<EntityStore> {
    private Card card;

    public CardComponent() {
    }

    public CardComponent(Card card) {
        this.card = card;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public static ComponentType<EntityStore, CardComponent> getComponentType() {
        return DuelsPlugin.cardComponent;
    }

    @Override
    public @Nullable Component<EntityStore> clone() {
        return this;
    }
}
