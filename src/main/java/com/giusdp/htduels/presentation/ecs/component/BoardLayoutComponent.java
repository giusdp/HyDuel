package com.giusdp.htduels.presentation.ecs.component;

import com.giusdp.htduels.DuelsPlugin;
import com.giusdp.htduels.presentation.layout.BoardLayout;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.Nullable;

public class BoardLayoutComponent implements Component<EntityStore> {
    private final BoardLayout boardLayout;

    public BoardLayoutComponent() {
        this.boardLayout = null;
    }

    public BoardLayoutComponent(BoardLayout boardLayout) {
        this.boardLayout = boardLayout;
    }

    public BoardLayout getBoardLayout() {
        return boardLayout;
    }

    public static ComponentType<EntityStore, BoardLayoutComponent> getComponentType() {
        return DuelsPlugin.boardLayoutComponent;
    }

    @Override
    public @Nullable Component<EntityStore> clone() {
        return this;
    }
}
