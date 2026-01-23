package com.giusdp.htduels.command;

import com.giusdp.htduels.component.CardComponent;
import com.hypixel.hytale.component.AddReason;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.shape.Box;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.model.config.Model;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.component.BoundingBox;
import com.hypixel.hytale.server.core.modules.entity.component.ModelComponent;
import com.hypixel.hytale.server.core.modules.entity.component.PersistentModel;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.tracker.NetworkId;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Command to spawn a test card entity in front of the player.
 */
public class SpawnCardCommand extends CommandBase {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public static Vector3d spawnPosition;
    public static Box spawnBoundingBox;

    public SpawnCardCommand() {
        super("spawncard", "Spawns a test card in front of you");
        this.setPermissionGroup(GameMode.Adventure);
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        if (!ctx.isPlayer()) {
            ctx.sendMessage(Message.raw("Only players can use this command!"));
            return;
        }

        Player player = ctx.senderAs(Player.class);
        World world = player.getWorld();

        if (world == null) {
            ctx.sendMessage(Message.raw("World is null!"));
            return;
        }

        // Spawn card in front of player
        world.execute(() -> {
            EntityStore entityStore = world.getEntityStore();

            // Get player position
            @SuppressWarnings("deprecation")
            Vector3d playerPos = player.getTransformComponent().getPosition();
            Vector3d cardPos = playerPos.clone().add(0, 1, -1); // 1 block up, 1 block forward
            spawnPosition = cardPos;

        // Create entity holder
            Holder<EntityStore> holder = EntityStore.REGISTRY.newHolder();

            // Transform component
            holder.addComponent(TransformComponent.getComponentType(), new TransformComponent(cardPos, new Vector3f()));

            // UUID component
            holder.addComponent(UUIDComponent.getComponentType(), new UUIDComponent(UUID.randomUUID()));

            // Network ID component
            holder.putComponent(NetworkId.getComponentType(), new NetworkId(entityStore.takeNextNetworkId()));

            // Load Card model
            ModelAsset modelAsset = ModelAsset.getAssetMap().getAsset("Card");
            if (modelAsset == null) {
                LOGGER.at(Level.SEVERE).log("Card model not found!");
                return;
            }

            Model model = Model.createScaledModel(modelAsset, 0.23f);
            holder.addComponent(PersistentModel.getComponentType(), new PersistentModel(model.toReference()));
            holder.addComponent(ModelComponent.getComponentType(), new ModelComponent(model));

            if (model.getBoundingBox() != null) {
                holder.addComponent(BoundingBox.getComponentType(), new BoundingBox(model.getBoundingBox()));
                LOGGER.at(Level.INFO).log("Bounding box details: %s", model.getBoundingBox().toString());
            }
            spawnBoundingBox = model.getBoundingBox();

            // Card component (our custom component!)
            CardComponent card = new CardComponent();
            holder.addComponent(CardComponent.getComponentType(), card);

            // Make it interactable
            // holder.ensureComponent(Interactable.getComponentType());

            Store<EntityStore> store = entityStore.getStore();
            // Spawn the entity
            store.addEntity(holder, AddReason.SPAWN);

            LOGGER.atInfo().log("Card spawned at position (%f, %f, %f) with name: %s", cardPos.x, cardPos.y, cardPos.z,
                    card.name);
        });

        ctx.sendMessage(Message.raw("Card spawned!"));
    }
}
