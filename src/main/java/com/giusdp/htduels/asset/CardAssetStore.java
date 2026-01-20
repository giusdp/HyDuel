package com.giusdp.htduels.asset;

import com.hypixel.hytale.assetstore.AssetStore;
import com.hypixel.hytale.assetstore.AssetUpdateQuery;
import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.event.IEventBus;
import com.hypixel.hytale.server.core.HytaleServer;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

public class CardAssetStore extends AssetStore<String, CardAsset, DefaultAssetMap<String, CardAsset>> {
    public CardAssetStore(Builder builder) {
        super(builder);
    }

    @Override
    protected IEventBus getEventBus() {
        return HytaleServer.get().getEventBus();
    }

    @Override
    public void addFileMonitor(@Nonnull String packKey, Path path) {
    }

    @Override
    public void removeFileMonitor(Path path) {
    }

    @Override
    protected void handleRemoveOrUpdate(Set<String> removed, Map<String, CardAsset> updated, @Nonnull AssetUpdateQuery query) {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends AssetStore.Builder<String, CardAsset, DefaultAssetMap<String, CardAsset>, Builder> {
        public Builder() {
            super(String.class, CardAsset.class, new DefaultAssetMap<>());
        }

        @Override
        public CardAssetStore build() {
            return new CardAssetStore(this);
        }
    }
}
