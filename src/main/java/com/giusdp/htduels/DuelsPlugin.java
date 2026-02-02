package com.giusdp.htduels;

import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.asset.CardAssetCodec;
import com.giusdp.htduels.asset.CardAssetStore;
import com.giusdp.htduels.command.duel.DuelCommand;
import com.giusdp.htduels.component.BoardLayoutComponent;
import com.giusdp.htduels.component.CardComponent;
import com.giusdp.htduels.component.CardDragComponent;
import com.giusdp.htduels.component.CardHoverComponent;
import com.giusdp.htduels.component.CardSpatialComponent;
import com.giusdp.htduels.component.DuelComponent;
import com.giusdp.htduels.handlers.PlayerMouseButtonHandler;
import com.giusdp.htduels.handlers.PlayerMouseMotionHandler;
import com.giusdp.htduels.interaction.BoardInteraction;
import com.giusdp.htduels.interaction.DuelSetupService;
import com.giusdp.htduels.interaction.InteractionNames;
import com.giusdp.htduels.system.DomainEventSync;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.giusdp.htduels.system.CardDragSystem;
import com.giusdp.htduels.system.CardHoverSystem;
import com.giusdp.htduels.system.CardMovementSystem;
import com.giusdp.htduels.system.CardRotationSystem;
import com.giusdp.htduels.system.CardSpatialResolutionSystem;
import com.giusdp.htduels.system.DuelTicker;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.event.events.player.PlayerMouseButtonEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerMouseMotionEvent;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.SimpleBlockInteraction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class DuelsPlugin extends JavaPlugin {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public static CardAssetStore cardAssetStore;

    public static ComponentType<EntityStore, CardComponent> cardComponent;
    public static ComponentType<EntityStore, CardDragComponent> cardDragComponent;
    public static ComponentType<EntityStore, CardHoverComponent> cardHoverComponent;
    public static ComponentType<EntityStore, DuelComponent> duelComponent;
    public static ComponentType<EntityStore, CardSpatialComponent> cardSpatialComponent;
    public static ComponentType<EntityStore, BoardLayoutComponent> boardLayoutComponent;

    public DuelsPlugin(JavaPluginInit init) {
        super(init);
        LOGGER.atInfo().log("Hello from %s version %s", this.getName(), this.getManifest().getVersion().toString());
    }

    @Override
    protected void setup() {
        DuelRegistry registry = new DuelRegistry();
        DuelSetupService duelSetupService = new DuelSetupService(registry);
        DuelCleanupService cleanupService = new DuelCleanupService(registry);
        DomainEventSync domainEventSync = new DomainEventSync(registry);

        setupComponents();

        // Systems
        this.getEntityStoreRegistry().registerSystem(new DuelTicker(cleanupService, registry, domainEventSync));
        this.getEntityStoreRegistry().registerSystem(new CardSpatialResolutionSystem());
        this.getEntityStoreRegistry().registerSystem(new CardMovementSystem());
        this.getEntityStoreRegistry().registerSystem(new CardRotationSystem());
        this.getEntityStoreRegistry().registerSystem(new CardDragSystem(registry));
        this.getEntityStoreRegistry().registerSystem(new CardHoverSystem());

        // Commands
        this.getCommandRegistry().registerCommand(new DuelCommand(registry));

        // Event handlers
        var mouseButtonHandler = new PlayerMouseButtonHandler(registry);
        var mouseMotionHandler = new PlayerMouseMotionHandler(registry);
        this.getEventRegistry().registerGlobal(PlayerMouseButtonEvent.class, mouseButtonHandler::handleMouseClick);
        this.getEventRegistry().registerGlobal(PlayerMouseMotionEvent.class, mouseMotionHandler::handleMouseMotion);

        setupInteractions(duelSetupService, registry);
        setupCardAssetStore();

        LOGGER.atInfo().log("HyDuels plugin ready.");
    }

    private void setupComponents() {
        cardComponent = this.getEntityStoreRegistry().registerComponent(CardComponent.class, CardComponent::new);
        cardDragComponent = this.getEntityStoreRegistry().registerComponent(CardDragComponent.class, CardDragComponent::new);
        cardHoverComponent = this.getEntityStoreRegistry().registerComponent(CardHoverComponent.class, CardHoverComponent::new);
        cardSpatialComponent = this.getEntityStoreRegistry().registerComponent(CardSpatialComponent.class, CardSpatialComponent::new);
        boardLayoutComponent = this.getEntityStoreRegistry().registerComponent(BoardLayoutComponent.class, BoardLayoutComponent::new);

        duelComponent = this.getEntityStoreRegistry().registerComponent(DuelComponent.class, DuelComponent::new);
    }

    private void setupInteractions(DuelSetupService duelSetupService, DuelRegistry registry) {
        BuilderCodec<BoardInteraction> boardInteractionCodec = BuilderCodec
                .builder(BoardInteraction.class, () -> new BoardInteraction(duelSetupService, registry), SimpleBlockInteraction.CODEC)
                .documentation("Goes to the duel board top-down camera view").build();

        this.getCodecRegistry(Interaction.CODEC).register(InteractionNames.BOARD_INTERACTION, BoardInteraction.class,
                boardInteractionCodec);
    }

    private void setupCardAssetStore() {
        cardAssetStore = CardAssetStore.builder().setPath("Cards").setCodec(CardAssetCodec.INSTANCE)
                .setKeyFunction(CardAsset::id).build();

        this.getAssetRegistry().register(cardAssetStore);

        LOGGER.atInfo().log("Loaded %d cards", cardAssetStore.getAssetMap().getAssetCount());
    }
}
