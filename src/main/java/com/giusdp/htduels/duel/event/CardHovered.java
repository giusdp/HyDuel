package com.giusdp.htduels.duel.event;

import com.giusdp.htduels.duel.Card;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duelist.Duelist;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.NonNull;

public class CardHovered extends DuelEvent {
    public final Ref<EntityStore> cardRef;
    public final Duelist viewer;

    public CardHovered(@NonNull Duel duel, @NonNull Ref<EntityStore> cardRef, @NonNull Duelist viewer) {
        super(duel);
        this.cardRef = cardRef;
        this.viewer = viewer;
    }
}
