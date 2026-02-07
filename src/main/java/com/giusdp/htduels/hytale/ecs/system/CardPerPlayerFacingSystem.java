package com.giusdp.htduels.hytale.ecs.system;

import com.giusdp.htduels.hytale.ecs.component.CardComponent;
import com.giusdp.htduels.hytale.ecs.component.CardOwnerComponent;
import com.giusdp.htduels.hytale.visibility.CardVisibilityRules;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.dependency.Dependency;
import com.hypixel.hytale.component.dependency.Order;
import com.hypixel.hytale.component.dependency.SystemGroupDependency;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.protocol.ComponentUpdate;
import com.hypixel.hytale.protocol.ComponentUpdateType;
import com.hypixel.hytale.protocol.Direction;
import com.hypixel.hytale.protocol.ModelTransform;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.tracker.EntityTrackerSystems;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Objects;
import java.util.Set;

public class CardPerPlayerFacingSystem extends EntityTickingSystem<EntityStore> {

    private static final float EPSILON = 1e-4f;
    private static final Set<Dependency<EntityStore>> DEPENDENCIES = Set.of(
            new SystemGroupDependency<>(Order.AFTER, EntityTrackerSystems.QUEUE_UPDATE_GROUP)
    );

    @Override
    public @NonNull Set<Dependency<EntityStore>> getDependencies() {
        return DEPENDENCIES;
    }

    @Override
    public @Nullable Query<EntityStore> getQuery() {
        return Query.and(
                CardComponent.getComponentType(),
                CardOwnerComponent.getComponentType()
        );
    }

    @Override
    public void tick(float deltaTime, int index, @NonNull ArchetypeChunk<EntityStore> archetypeChunk,
                     @NonNull Store<EntityStore> store, @NonNull CommandBuffer<EntityStore> commandBuffer) {
        CardComponent cardComp = archetypeChunk.getComponent(index, CardComponent.getComponentType());
        CardOwnerComponent ownerComp = archetypeChunk.getComponent(index, CardOwnerComponent.getComponentType());
        EntityTrackerSystems.Visible visible = archetypeChunk.getComponent(index, EntityTrackerSystems.Visible.getComponentType());
        TransformComponent transform = archetypeChunk.getComponent(index, TransformComponent.getComponentType());

        if (cardComp == null || ownerComp == null || visible == null || transform == null) {
            return;
        }

        // Get what was just broadcast by TransformSystems.EntityTrackerUpdate
        ModelTransform sentTransform = transform.getSentTransform();
        if (sentTransform.bodyOrientation == null) {
            return;
        }

        var zoneType = cardComp.getZoneType();
        if (zoneType == null) {
            return;
        }

        float broadcastPitch = sentTransform.bodyOrientation.pitch;
        float broadcastYaw = sentTransform.bodyOrientation.yaw;
        float roll = sentTransform.bodyOrientation.roll;

        boolean opponentSide = cardComp.isOpponentSide();
        Ref<EntityStore> cardRef = archetypeChunk.getReferenceTo(index);

        // For each viewer, check if the broadcast pitch/yaw differs from what they should see
        for (var entry : visible.visibleTo.entrySet()) {
            Ref<EntityStore> viewerRef = entry.getKey();
            EntityTrackerSystems.EntityViewer viewer = entry.getValue();

            PlayerRef viewerPlayerRef = store.getComponent(viewerRef, PlayerRef.getComponentType());
            boolean isOwner = viewerPlayerRef != null && Objects.equals(viewerPlayerRef, ownerComp.getOwnerPlayerRef());

            float expectedPitch = CardVisibilityRules.resolvePitch(zoneType, isOwner);

            boolean viewerOnOpponentSide = isOwner == opponentSide;
            float expectedYaw = CardVisibilityRules.resolveYaw(broadcastYaw, viewerOnOpponentSide);

            // Only send override if broadcast pitch or yaw differs from expected
            if (Math.abs(broadcastPitch - expectedPitch) > EPSILON || Math.abs(broadcastYaw - expectedYaw) > EPSILON) {
                ComponentUpdate update = new ComponentUpdate();
                update.type = ComponentUpdateType.Transform;
                update.transform = new ModelTransform();
                // Copy position from the broadcast, only override orientation
                update.transform.position = sentTransform.position;
                update.transform.lookOrientation = sentTransform.lookOrientation;
                update.transform.bodyOrientation = new Direction(expectedYaw, expectedPitch, roll);

                viewer.queueUpdate(cardRef, update);
            }
        }
    }
}
