package com.giusdp.htduels.asset;

import com.hypixel.hytale.assetstore.codec.AssetCodec;
import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.server.core.asset.HytaleAssetStore;

import javax.annotation.Nonnull;
import java.util.function.Function;

public class CardAssetStore extends HytaleAssetStore<String, CardAsset, DefaultAssetMap<String, CardAsset>> {
    public CardAssetStore(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder
            extends
            HytaleAssetStore.Builder<String, CardAsset, DefaultAssetMap<String, CardAsset>> {
        public Builder() {
            super(String.class, CardAsset.class, new DefaultAssetMap<>());
        }

        @Nonnull
        @Override
        public Builder setPath(@Nonnull String path) {
            super.setPath(path);
            return this;
        }

        @Nonnull
        @Override
        public Builder setCodec(@Nonnull AssetCodec<String, CardAsset> codec) {
            super.setCodec(codec);
            return this;
        }

        @Nonnull
        @Override
        public Builder setKeyFunction(@Nonnull Function<CardAsset, String> keyFunction) {
            super.setKeyFunction(keyFunction);
            return this;
        }

        @Nonnull
        @Override
        public CardAssetStore build() {
            return new CardAssetStore(this);
        }
    }
}
