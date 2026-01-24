package com.giusdp.htduels.component;

import com.giusdp.htduels.CardAssetRepo;
import com.giusdp.htduels.DuelsPlugin;
import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.eventbus.HytaleEventBus;
import com.giusdp.htduels.duel.positioning.BoardLayout;
import com.giusdp.htduels.duelist.Bot;
import com.giusdp.htduels.duelist.DuelPlayer;
import com.giusdp.htduels.duelist.Duelist;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.math.Vec2f;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class DuelComponent implements Component<EntityStore> {
    // Static reference to the currently active duel for command access
    private static DuelComponent activeDuel;
    public Duel duel;

    public DuelComponent() {
        this(new Vector3i(0, 0, 0));
    }

    public DuelComponent(Vector3i boardPosition) {
        Duelist player = new DuelPlayer();
        Duelist bot = new Bot();
        BoardLayout boardLayout = createBoardLayout(boardPosition);
        duel = new Duel(player, bot, new HytaleEventBus(), new CardAssetRepo(), boardLayout);
        activeDuel = this;

        duel.setup();
    }

    private BoardLayout createBoardLayout(Vector3i boardPosition) {
        float baseX = boardPosition.x;
        float baseZ = boardPosition.z;

        return new BoardLayout(
                new Vec2f(baseX, baseZ - 0.5f),  // playerBottomBattlefieldCenter
                new Vec2f(baseX, baseZ + 0.5f),  // playerTopBattlefieldCenter
                new Vec2f(baseX, baseZ - 1.0f),  // playerBottomHandCenter
                new Vec2f(baseX, baseZ + 1.0f),  // playerTopHandCenter
                new Vec2f(baseX - 1.5f, baseZ - 1.0f), // playerBottomDeckPosition
                new Vec2f(baseX - 1.5f, baseZ + 1.0f), // playerTopDeckPosition
                0.3f,  // battlefieldSpacing
                0.25f, // handSpacing
                0.2f,  // battlefieldCardWidth
                0.15f  // handCardWidth
        );
    }

    public static DuelComponent getActiveDuel() {
        return activeDuel;
    }

    public static ComponentType<EntityStore, DuelComponent> getComponentType() {
        return DuelsPlugin.duelComponent;
    }

    @Override
    public Component<EntityStore> clone() {
        return this;
    }
}
