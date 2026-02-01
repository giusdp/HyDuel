package com.giusdp.htduels.interaction;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.Rotation;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public record BoardContext(Vector3i boardPosition, Rotation boardRotation, Ref<EntityStore> playerEntityRef) {
}
