package com.giusdp.htduels.spawn;

import com.giusdp.htduels.component.CardComponent;
import com.giusdp.htduels.component.CardSpatialComponent;
import com.giusdp.htduels.duel.Card;
import com.hypixel.hytale.component.AddReason;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.asset.type.model.config.Model;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.modules.entity.component.BoundingBox;
import com.hypixel.hytale.server.core.modules.entity.component.ModelComponent;
import com.hypixel.hytale.server.core.modules.entity.component.PersistentModel;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.tracker.NetworkId;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public final class CardSpawner {
    private static final String CARD_MODEL_NAME = "Card";
    private static final float CARD_MODEL_SCALE = 0.23f;

    private CardSpawner() {
    }

    @Nullable
    public static Ref<EntityStore> spawn(
            @Nonnull CommandBuffer<EntityStore> commandBuffer,
            @Nonnull Ref<EntityStore> duelEntityRef,
            @Nonnull Card card,
            @Nonnull Vector3d position
    ) {
        ModelAsset modelAsset = ModelAsset.getAssetMap().getAsset(CARD_MODEL_NAME);
        if (modelAsset == null) {
            return null;
        }

        Holder<EntityStore> holder = EntityStore.REGISTRY.newHolder();

        // Position
        holder.addComponent(
                TransformComponent.getComponentType(),
                new TransformComponent(position, new Vector3f())
        );

        // Identity
        holder.addComponent(UUIDComponent.getComponentType(), new UUIDComponent(UUID.randomUUID()));
        holder.putComponent(NetworkId.getComponentType(), new NetworkId(duelEntityRef.getStore().getExternalData().takeNextNetworkId()));

        // Model
        Model model = Model.createScaledModel(modelAsset, CARD_MODEL_SCALE);
        holder.addComponent(PersistentModel.getComponentType(), new PersistentModel(model.toReference()));
        holder.addComponent(ModelComponent.getComponentType(), new ModelComponent(model));

        if (model.getBoundingBox() != null) {
            holder.addComponent(BoundingBox.getComponentType(), new BoundingBox(model.getBoundingBox()));
        }

        // Game data
        holder.addComponent(CardComponent.getComponentType(), new CardComponent(card, duelEntityRef));
        holder.addComponent(CardSpatialComponent.getComponentType(), new CardSpatialComponent());

        return commandBuffer.addEntity(holder, AddReason.SPAWN);
    }
}
