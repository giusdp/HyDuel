package com.giusdp.htduels;

import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.asset.CardAssetCodec;
import com.giusdp.htduels.asset.CardAssetStore;
import com.giusdp.htduels.command.ResetCameraCommand;
import com.giusdp.htduels.command.duel.DuelCommand;
import com.giusdp.htduels.component.BoardLayoutComponent;
import com.giusdp.htduels.component.CardComponent;
import com.giusdp.htduels.component.CardSpatialComponent;
import com.giusdp.htduels.component.DuelComponent;
import com.giusdp.htduels.handlers.PlayerMouseButtonHandler;
import com.giusdp.htduels.handlers.PlayerMouseMotionHandler;
import com.giusdp.htduels.interaction.BoardInteraction;
import com.giusdp.htduels.interaction.InteractionNames;
import com.giusdp.htduels.system.CardMovementSystem;
import com.giusdp.htduels.system.CardSpatialResolutionSystem;
import com.giusdp.htduels.system.DuelTicker;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.event.events.player.PlayerMouseButtonEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerMouseMotionEvent;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class DuelsPlugin extends JavaPlugin {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public static CardAssetStore cardAssetStore;

    public static ComponentType<EntityStore, CardComponent> cardComponent;
    public static ComponentType<EntityStore, DuelComponent> duelComponent;
    public static ComponentType<EntityStore, CardSpatialComponent> cardSpatialComponent;
    public static ComponentType<EntityStore, BoardLayoutComponent> boardLayoutComponent;

    public DuelsPlugin(JavaPluginInit init) {
        super(init);
        LOGGER.atInfo().log("Hello from %s version %s", this.getName(), this.getManifest().getVersion().toString());
    }

    @Override
    protected void setup() {
        setupComponents();
        setupSystems();

        setupCommands();

        setupInteractions();
        setupEventHandlers();

        setupCardAssetStore();

        LOGGER.atInfo().log("HyDuels plugin ready.");
    }

    private void setupComponents() {
        cardComponent = this.getEntityStoreRegistry().registerComponent(CardComponent.class, CardComponent::new);
        cardSpatialComponent = this.getEntityStoreRegistry().registerComponent(CardSpatialComponent.class, CardSpatialComponent::new);
        boardLayoutComponent = this.getEntityStoreRegistry().registerComponent(BoardLayoutComponent.class, BoardLayoutComponent::new);

        duelComponent = this.getEntityStoreRegistry().registerComponent(DuelComponent.class, DuelComponent::new);
    }

    private void setupCommands() {
        this.getCommandRegistry().registerCommand(new DuelCommand());

        this.getCommandRegistry().registerCommand(new ResetCameraCommand());
    }

    private void setupInteractions() {
        this.getCodecRegistry(Interaction.CODEC).register(InteractionNames.BOARD_INTERACTION, BoardInteraction.class,
                BoardInteraction.CODEC);

    }

    private void setupEventHandlers() {
        this.getEventRegistry().registerGlobal(PlayerMouseButtonEvent.class, PlayerMouseButtonHandler::handleMouseClick);
        this.getEventRegistry().registerGlobal(PlayerMouseMotionEvent.class, PlayerMouseMotionHandler::handleMouseMotion);


    }

    private void setupSystems() {
        this.getEntityStoreRegistry().registerSystem(new DuelTicker());
        this.getEntityStoreRegistry().registerSystem(new CardSpatialResolutionSystem());
        this.getEntityStoreRegistry().registerSystem(new CardMovementSystem());
    }

    private void setupCardAssetStore() {
        cardAssetStore = CardAssetStore.builder().setPath("Cards").setCodec(CardAssetCodec.INSTANCE)
                .setKeyFunction(CardAsset::id).build();

        this.getAssetRegistry().register(cardAssetStore);

        LOGGER.atInfo().log("Loaded %d cards", cardAssetStore.getAssetMap().getAssetCount());
    }
}
