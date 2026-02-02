package com.giusdp.htduels.match;

import java.util.UUID;

public record CardId(UUID value) {
    public static CardId generate() { return new CardId(UUID.randomUUID()); }
}
