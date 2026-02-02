package com.giusdp.htduels.duel;

import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.duel.zone.Zone;
import com.giusdp.htduels.duel.zone.ZoneType;
import com.giusdp.htduels.duelist.Duelist;

public class Card {
    private final CardId id;
    private final CardAsset asset;
    private Duelist owner;
    private Zone zone;

    public Card(CardId id, CardAsset asset) {
        this.id = id;
        this.asset = asset;
    }

    public Card(CardAsset asset) {
        this(CardId.generate(), asset);
    }

    public CardId getId() {
        return id;
    }

    public CardAsset getAsset() {
        return asset;
    }

    public Zone getZone() {
        return zone;
    }

    public ZoneType getCurrentZoneType() {
        return zone != null ? zone.getType() : null;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public int getZoneIndex() {
        if (zone == null) {
            return -1;
        }
        return zone.getCards().indexOf(this);
    }

    public Duelist getOwner() {
        return owner;
    }

    public void setOwner(Duelist owner) {
        this.owner = owner;
    }
}
