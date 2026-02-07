package com.giusdp.htduels.hytale.ecs;

import com.giusdp.htduels.hytale.ecs.component.CardComponent;
import com.giusdp.htduels.hytale.ecs.component.CardDragComponent;
import com.giusdp.htduels.hytale.ecs.component.CardHoverComponent;
import com.giusdp.htduels.hytale.ecs.component.CardOwnerComponent;
import com.giusdp.htduels.hytale.ecs.component.CardSpatialComponent;
import com.giusdp.htduels.match.CardId;
import com.giusdp.htduels.match.zone.ZoneType;
import com.hypixel.hytale.server.core.universe.PlayerRef;
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
            @Nonnull CardId cardId,
            @Nonnull ZoneType zoneType,
            int zoneIndex,
            int zoneSize,
            boolean opponentSide,
            @Nonnull Vector3d position,
            @Nonnull Vector3f rotation,
            @Nullable PlayerRef ownerPlayerRef
    ) {
        ModelAsset modelAsset = ModelAsset.getAssetMap().getAsset(CARD_MODEL_NAME);
        if (modelAsset == null) {
            return null;
        }

        Holder<EntityStore> holder = EntityStore.REGISTRY.newHolder();

        // Position and rotation
        holder.addComponent(
                TransformComponent.getComponentType(),
                new TransformComponent(position, rotation)
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
        holder.addComponent(CardComponent.getComponentType(), new CardComponent(cardId, duelEntityRef, zoneType, zoneIndex, zoneSize, opponentSide));
        holder.addComponent(CardDragComponent.getComponentType(), new CardDragComponent());
        holder.addComponent(CardHoverComponent.getComponentType(), new CardHoverComponent());
        holder.addComponent(CardSpatialComponent.getComponentType(), new CardSpatialComponent());
        holder.addComponent(CardOwnerComponent.getComponentType(), new CardOwnerComponent(ownerPlayerRef));

        return commandBuffer.addEntity(holder, AddReason.SPAWN);
    }
}
