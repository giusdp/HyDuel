package com.giusdp.htduels.interaction;

import com.giusdp.htduels.CardAssetRepo;
import com.giusdp.htduels.DuelistContext;
import com.giusdp.htduels.component.BoardLayoutComponent;
import com.giusdp.htduels.component.DuelComponent;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.positioning.BoardLayout;
import com.giusdp.htduels.duelist.Bot;
import com.giusdp.htduels.duelist.DuelPlayer;
import com.giusdp.htduels.duelist.Duelist;
import com.giusdp.htduels.ui.BoardGameUi;
import com.hypixel.hytale.component.AddReason;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;

import com.hypixel.hytale.math.Vec2f;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.*;
import com.hypixel.hytale.protocol.packets.camera.SetServerCamera;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.Rotation;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.HashMap;
import java.util.Map;

public final class DuelSetupService {

    private static final double Y_OFFSET = 1.75;
    private static final float CAMERA_PITCH = (float) Math.toRadians(-90.0f);
    public static final float CARD_Y_OFFSET = 1.1f;

    private static final Map<Vector3i, Ref<EntityStore>> activeDuels = new HashMap<>();

    private DuelSetupService() {}

    public static Ref<EntityStore> createAndSpawnDuel(BoardContext ctx, Store<EntityStore> store) {
        Vector3i boardPosition = ctx.boardPosition();
        Rotation boardRotation = ctx.boardRotation();

        Duel duel = Duel.builder()
                .cardRepo(new CardAssetRepo())
                .boardPosition(boardPosition)
                .build();

        BoardLayout layout = createBoardLayout(boardPosition, boardRotation);
        Ref<EntityStore> duelRef = spawnDuelEntity(store, duel, layout);

        activeDuels.put(boardPosition, duelRef);
        return duelRef;
    }

    public static void joinAsPlayer(PlayerRef playerRef, BoardContext ctx, Store<EntityStore> store, Ref<EntityStore> duelRef) {
        Vector3i boardPosition = ctx.boardPosition();
        Rotation boardRotation = ctx.boardRotation();
        Ref<EntityStore> playerEntityRef = ctx.playerEntityRef();

        DuelComponent duelComp = store.getComponent(duelRef, DuelComponent.getComponentType());
        assert duelComp != null;
        Duel duel = duelComp.duel;

        DuelPlayer humanDuelist = new DuelPlayer();
        boolean isOpponentSide = !duel.getDuelists().isEmpty();
        humanDuelist.setOpponentSide(isOpponentSide);
        duel.addDuelist(humanDuelist);

        float cameraYaw = (float) boardRotation.getRadians();
        if (isOpponentSide) {
            cameraYaw += (float) Math.PI;
        }

        Position cameraPos = calculateCameraPosition(boardPosition, boardRotation);
        float cardY = boardPosition.y + CARD_Y_OFFSET;

        Player player = store.getComponent(playerEntityRef, Player.getComponentType());
        assert player != null;

        activateBoardCamera(playerRef, cameraPos, cameraYaw);
        registerPlayer(playerRef, duelRef, duel, humanDuelist, cameraPos, cameraYaw, cardY, player, playerEntityRef);
    }

    public static void joinAsBot(Ref<EntityStore> duelRef, Store<EntityStore> store) {
        DuelComponent duelComp = store.getComponent(duelRef, DuelComponent.getComponentType());
        assert duelComp != null;
        Duel duel = duelComp.duel;

        Bot botDuelist = new Bot();
        boolean isOpponentSide = !duel.getDuelists().isEmpty();
        botDuelist.setOpponentSide(isOpponentSide);
        duel.addDuelist(botDuelist);

        registerBot(duelRef, duel, botDuelist);
    }

    public static Ref<EntityStore> findDuelAt(Vector3i boardPosition) {
        return activeDuels.get(boardPosition);
    }

    public static void removeDuel(Vector3i boardPosition) {
        activeDuels.remove(boardPosition);
    }

    // --- Building blocks ---

    public static Ref<EntityStore> spawnDuelEntity(Store<EntityStore> store, Duel duel, BoardLayout layout) {
        Holder<EntityStore> holder = EntityStore.REGISTRY.newHolder();
        holder.addComponent(DuelComponent.getComponentType(), new DuelComponent(duel));
        holder.addComponent(BoardLayoutComponent.getComponentType(), new BoardLayoutComponent(layout));
        return store.addEntity(holder, AddReason.SPAWN);
    }

    public static void registerPlayer(PlayerRef playerRef, Ref<EntityStore> duelRef,
                                      Duel duel, Duelist duelist, Position cameraPos,
                                      float cameraYaw, float cardY,
                                      Player player, Ref<EntityStore> playerEntityRef) {
        DuelistContext ctx = DuelistContext.registerGlobal(playerRef, duelRef, duelist, cameraPos, cameraYaw, cardY);
        BoardGameUi boardGameUi = activateBoardUI(player, playerRef, playerEntityRef);
        ctx.setBoardGameUi(boardGameUi);
        duel.addContext(ctx);
    }

    public static void registerBot(Ref<EntityStore> duelRef, Duel duel, Duelist duelist) {
        DuelistContext botCtx = new DuelistContext(duelRef, duelist);
        duel.addContext(botCtx);
    }

    public static void activateBoardCamera(PlayerRef playerRef, Position cameraPos, float yaw) {
        ServerCameraSettings settings = createBoardCameraSettings(cameraPos, yaw);
        playerRef.getPacketHandler().writeNoCache(new SetServerCamera(ClientCameraView.Custom, true, settings));
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

    public static Position calculateCameraPosition(Vector3i boardPosition, Rotation rotation) {
        double centerX = boardPosition.x;
        double centerY = boardPosition.y + Y_OFFSET;
        double centerZ = boardPosition.z;

        switch (rotation) {
            case None -> centerZ += 0.5;
            case Ninety -> {
                centerX += 0.5;
                centerZ += 1;
            }
            case OneEighty -> {
                centerX += 1;
                centerZ += 0.5;
            }
            case TwoSeventy -> centerX += 0.5;
        }

        return new Position(centerX, centerY, centerZ);
    }

    private static ServerCameraSettings createBoardCameraSettings(Position cameraPosition, float yaw) {
        ServerCameraSettings settings = new ServerCameraSettings();

        settings.positionType = PositionType.Custom;
        settings.position = cameraPosition;

        settings.rotationType = RotationType.Custom;
        settings.rotation = new Direction(yaw, CAMERA_PITCH, 0);

        settings.movementMultiplier = new com.hypixel.hytale.protocol.Vector3f(0, 0, 0);
        settings.applyMovementType = ApplyMovementType.Position;

        settings.displayCursor = true;
        settings.displayReticle = false;
        settings.skipCharacterPhysics = true;
        settings.isFirstPerson = false;

        settings.sendMouseMotion = true;
        settings.mouseInputType = MouseInputType.LookAtTargetEntity;
        settings.planeNormal = new com.hypixel.hytale.protocol.Vector3f(0, 1, 0);
        return settings;
    }
}
