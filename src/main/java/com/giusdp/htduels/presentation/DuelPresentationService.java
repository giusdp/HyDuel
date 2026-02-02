package com.giusdp.htduels.presentation;
import com.giusdp.htduels.match.DuelService;
import com.giusdp.htduels.match.DuelRegistry;
import com.giusdp.htduels.presentation.camera.BoardCameraService;

import com.giusdp.htduels.presentation.ecs.component.BoardLayoutComponent;
import com.giusdp.htduels.presentation.ecs.component.DuelComponent;
import com.giusdp.htduels.match.Duel;
import com.giusdp.htduels.match.DuelId;
import com.giusdp.htduels.presentation.layout.BoardLayout;
import com.giusdp.htduels.match.Duelist;
import com.giusdp.htduels.presentation.input.BoardContext;
import com.giusdp.htduels.presentation.input.CardInteractionService;
import com.giusdp.htduels.presentation.ui.BoardGameUi;
import com.hypixel.hytale.component.AddReason;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.RemoveReason;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.Vec2f;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.Position;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.Rotation;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class DuelPresentationService {

    public static final float CARD_Y_OFFSET = 1.1f;

    private final DuelService duelService;
    private final DuelRegistry registry;
    private final Map<PlayerRef, DuelistSessionManager> playerSessions = new HashMap<>();

    public DuelPresentationService(DuelService duelService, DuelRegistry registry) {
        this.duelService = duelService;
        this.registry = registry;
    }

    // --- Session management (moved from DuelRegistry) ---

    public void registerSession(PlayerRef playerRef, DuelistSessionManager session) {
        playerSessions.put(playerRef, session);
    }

    @Nullable
    public DuelistSessionManager getSession(PlayerRef playerRef) {
        return playerSessions.get(playerRef);
    }

    public void unregisterByDuelRef(Ref<EntityStore> duelRef) {
        playerSessions.values().removeIf(ctx -> ctx.getDuelRef().equals(duelRef));
    }

    // --- Duel creation and setup ---

    public Ref<EntityStore> createAndSpawnDuel(BoardContext ctx, Store<EntityStore> store) {
        Vector3i boardPosition = ctx.boardPosition();
        Rotation boardRotation = ctx.boardRotation();

        Duel duel = duelService.createDuel(boardPosition);

        BoardLayout layout = createBoardLayout(boardPosition, boardRotation);
        Ref<EntityStore> duelRef = spawnDuelEntity(store, duel, layout);

        registry.registerDuel(boardPosition, duelRef);
        return duelRef;
    }

    public void joinAsPlayer(PlayerRef playerRef, BoardContext ctx, Store<EntityStore> store, Ref<EntityStore> duelRef) {
        Vector3i boardPosition = ctx.boardPosition();
        Rotation boardRotation = ctx.boardRotation();
        Ref<EntityStore> playerEntityRef = ctx.playerEntityRef();

        DuelComponent duelComp = store.getComponent(duelRef, DuelComponent.getComponentType());
        assert duelComp != null;
        Duel duel = duelService.findDuel(duelComp.getDuelId());
        assert duel != null;

        Duelist humanDuelist = duelService.addHumanDuelist(duel);
        boolean isOpponentSide = humanDuelist.isOpponentSide();

        float cameraYaw = (float) boardRotation.getRadians();
        if (isOpponentSide) {
            cameraYaw += (float) Math.PI;
        }

        Position cameraPos = BoardCameraService.calculateCameraPosition(boardPosition, boardRotation);
        float cardY = boardPosition.y + CARD_Y_OFFSET;

        Player player = store.getComponent(playerEntityRef, Player.getComponentType());
        assert player != null;

        BoardCameraService.activate(playerRef, cameraPos, cameraYaw);

        DuelistSessionManager session = new DuelistSessionManager(playerRef, duelRef, humanDuelist, duel, cameraPos, cameraYaw, cardY);
        registerSession(playerRef, session);
        BoardGameUi boardGameUi = activateBoardUI(player, playerRef, playerEntityRef);
        session.setBoardGameUi(boardGameUi);
        duel.addContext(session);
    }

    public void joinAsBot(Ref<EntityStore> duelRef, Store<EntityStore> store) {
        DuelComponent duelComp = store.getComponent(duelRef, DuelComponent.getComponentType());
        assert duelComp != null;
        Duel duel = duelService.findDuel(duelComp.getDuelId());
        assert duel != null;

        Duelist botDuelist = duelService.addBotDuelist(duel);

        DuelistSessionManager botSession = new DuelistSessionManager(duelRef, botDuelist, duel);
        duel.addContext(botSession);
    }

    @Nullable
    public Ref<EntityStore> findDuelAt(Vector3i boardPosition) {
        return registry.findDuelAt(boardPosition);
    }

    // --- Cleanup (absorbed from DuelCleanupService) ---

    public void cleanup(Ref<EntityStore> duelRef, DuelComponent duelComp, Duel duel, CommandBuffer<EntityStore> commandBuffer) {
        for (Ref<EntityStore> cardRef : duelComp.getCardEntities()) {
            commandBuffer.removeEntity(cardRef, RemoveReason.REMOVE);
        }

        for (DuelistSessionManager ctx : duel.getContexts()) {
            PlayerRef playerRef = ctx.getPlayerRef();
            if (playerRef == null) {
                continue;
            }
            dismissBoardGameUi(playerRef);
            BoardCameraService.resetToFirstPerson(playerRef);
            CardInteractionService.clearHoveredCard(playerRef);
        }

        Vector3i boardPosition = duel.getBoardPosition();
        if (boardPosition != null) {
            registry.removeDuel(boardPosition);
        }

        registry.removeDuelById(duelComp.getDuelId());
        commandBuffer.removeEntity(duelRef, RemoveReason.REMOVE);
        unregisterByDuelRef(duelRef);
    }

    // --- Building blocks ---

    public static Ref<EntityStore> spawnDuelEntity(Store<EntityStore> store, Duel duel, BoardLayout layout) {
        Holder<EntityStore> holder = EntityStore.REGISTRY.newHolder();
        holder.addComponent(DuelComponent.getComponentType(), new DuelComponent(duel.getId()));
        holder.addComponent(BoardLayoutComponent.getComponentType(), new BoardLayoutComponent(layout));
        return store.addEntity(holder, AddReason.SPAWN);
    }

    public static BoardGameUi activateBoardUI(Player player, PlayerRef playerRef, Ref<EntityStore> playerEntityRef) {
        BoardGameUi boardGameUi = new BoardGameUi(playerRef, CustomPageLifetime.CanDismiss);
        player.getPageManager().openCustomPage(playerEntityRef, playerEntityRef.getStore(), boardGameUi);
        return boardGameUi;
    }

    public static BoardLayout createBoardLayout(Vector3i boardPosition, Rotation rotation) {
        float originX = boardPosition.x;
        float originZ = boardPosition.z;
        switch (rotation) {
            case None -> originZ += 0.5f;
            case Ninety -> {
                originX += 0.5f;
                originZ += 1f;
            }
            case OneEighty -> {
                originX += 1f;
                originZ += 0.5f;
            }
            case TwoSeventy -> originX += 0.5f;
        }
        Vec2f origin = new Vec2f(originX, originZ);

        return new BoardLayout(
                origin,
                rotation,
                0.25f,
                0.25f,
                0.4f,
                0.4f,
                0.8f,
                0.15f,
                0.12f,
                0.2f,
                0.15f,
                boardPosition.y + 1.2f,
                boardPosition.y + 1.05f
        );
    }

    private static void dismissBoardGameUi(PlayerRef playerRef) {
        Ref<EntityStore> playerEntityRef = playerRef.getReference();
        if (playerEntityRef == null) {
            return;
        }

        Store<EntityStore> store = playerEntityRef.getStore();
        Player player = store.getComponent(playerEntityRef, Player.getComponentType());
        if (player != null) {
            player.getPageManager().setPage(playerEntityRef, store, Page.None);
        }
    }
}
