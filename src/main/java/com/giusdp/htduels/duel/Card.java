package com.giusdp.htduels.duel;

import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.duel.zone.Zone;
import com.giusdp.htduels.duelist.Duelist;

public class Card {
    private final CardAsset asset;
    private Duelist owner;
    private Zone zone;
    private Duel duel;

    public Card(CardAsset asset) {
        this.asset = asset;
    }

    public CardAsset getAsset() {
        return asset;
    }

    public Zone getZone() {
        return zone;
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

    public Duel getDuel() {
        return duel;
    }

    public void setDuel(Duel duel) {
        this.duel = duel;
    }

    public Duelist getOwner() {
        return owner;
    }

    public void setOwner(Duelist owner) {
        this.owner = owner;
    }
}
