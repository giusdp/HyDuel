package com.giusdp.htduels;

import com.giusdp.htduels.commands.DuelCommand;
import com.giusdp.htduels.commands.ResetCameraCommand;
import com.giusdp.htduels.commands.SpawnCardCommand;
import com.giusdp.htduels.components.CardComponent;
import com.giusdp.htduels.components.DuelComponent;
import com.giusdp.htduels.events.BoardMouseHandler;
import com.giusdp.htduels.interactions.BoardInteraction;
import com.giusdp.htduels.systems.DuelTicker;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.event.events.player.PlayerMouseButtonEvent;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class DuelsPlugin extends JavaPlugin {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public static ComponentType<EntityStore, CardComponent> cardComponent;
    public static ComponentType<EntityStore, DuelComponent> duelComponent;

    public DuelsPlugin(JavaPluginInit init) {
        super(init);
        LOGGER.atInfo().log("Hello from %s version %s", this.getName(), this.getManifest().getVersion().toString());
    }

    @Override
    protected void setup() {
        // Register custom components
        cardComponent = this.getEntityStoreRegistry().registerComponent(CardComponent.class, CardComponent::new);
        duelComponent = this.getEntityStoreRegistry().registerComponent(DuelComponent.class, DuelComponent::new);

        // Register commands
        this.getCommandRegistry().registerCommand(new DuelCommand());
        this.getCommandRegistry().registerCommand(new ResetCameraCommand());
        this.getCommandRegistry().registerCommand(new SpawnCardCommand());

        // Register custom interactions
        this.getCodecRegistry(Interaction.CODEC).register("BoardActivation", BoardInteraction.class, BoardInteraction.CODEC);

        // Register Event handlers
        this.getEventRegistry().registerGlobal(PlayerMouseButtonEvent.class, BoardMouseHandler::handleMouseClick);

        // Register Systems
        this.getEntityStoreRegistry().registerSystem(new DuelTicker());

        LOGGER.atInfo().log("HyDuels plugin ready.");

    }
}
