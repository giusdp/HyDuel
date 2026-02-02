package com.giusdp.htduels.catalog;

import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.assetstore.map.JsonAssetWithMap;

public record CardAsset(String id, String name, int cost, int attack, int health,
                        String type) implements JsonAssetWithMap<String, DefaultAssetMap<String, CardAsset>> {
    @Override
    public String getId() {
        return id;
    }
}
