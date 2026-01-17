package com.giusdp.htduels;

import com.giusdp.htduels.interactions.BoardInteraction;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

public class DuelsPlugin extends JavaPlugin {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public DuelsPlugin(JavaPluginInit init) {
        super(init);
        LOGGER.atInfo().log("Hello from %s version %s", this.getName(), this.getManifest().getVersion().toString());
    }

    @Override
    protected void setup() {
        // Register commands
        this.getCommandRegistry().registerCommand(new ResetCameraCommand());

        // Register custom interactions
        this.getCodecRegistry(Interaction.CODEC).register("BoardActivation", BoardInteraction.class, BoardInteraction.CODEC);

        LOGGER.atInfo().log("HytaleDuels plugin initialized successfully!");
    }
}
