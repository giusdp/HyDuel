package com.giusdp.htduels.ui;

import com.giusdp.htduels.interaction.BoardContext;
import com.giusdp.htduels.interaction.DuelSetupService;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public class DuelModeSelectionPage extends InteractiveCustomUIPage<DuelModeSelectionPage.ModeEventData> {

    private final BoardContext boardContext;

    public DuelModeSelectionPage(@Nonnull PlayerRef playerRef, BoardContext boardContext) {
        super(playerRef, CustomPageLifetime.CanDismiss, ModeEventData.CODEC);
        this.boardContext = boardContext;
    }

    @Override
    public void build(@Nonnull Ref<EntityStore> ref, @Nonnull UICommandBuilder uiCommandBuilder,
                      @Nonnull UIEventBuilder uiEventBuilder, @Nonnull Store<EntityStore> store) {
        uiCommandBuilder.append("Pages/DuelModeSelection.ui");

        uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#BotButton",
                EventData.of("Mode", "bot"), false);
        uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#PvpButton",
                EventData.of("Mode", "pvp"), false);
    }

    @Override
    public void handleDataEvent(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store,
                                @Nonnull ModeEventData data) {
        switch (data.mode) {
            case "bot" -> {
                Ref<EntityStore> duelRef = DuelSetupService.createAndSpawnDuel(boardContext, store);
                DuelSetupService.joinAsPlayer(playerRef, boardContext, store, duelRef);
                DuelSetupService.joinAsBot(duelRef, store);
            }
            case "pvp" -> {
                Ref<EntityStore> existing = DuelSetupService.findDuelAt(boardContext.boardPosition());
                if (existing == null) {
                    Ref<EntityStore> duelRef = DuelSetupService.createAndSpawnDuel(boardContext, store);
                    DuelSetupService.joinAsPlayer(playerRef, boardContext, store, duelRef);
                    return;
                }

                DuelSetupService.joinAsPlayer(playerRef, boardContext, store, existing);
            }
            case null, default -> {
            }
        }
    }

    public static class ModeEventData {
        public static final BuilderCodec<ModeEventData> CODEC = BuilderCodec.builder(ModeEventData.class, ModeEventData::new)
                .append(new KeyedCodec<>("Mode", Codec.STRING), (d, v) -> d.mode = v, d -> d.mode)
                .add()
                .build();

        public String mode;
    }
}
