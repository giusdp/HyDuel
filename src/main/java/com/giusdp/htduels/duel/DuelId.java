package com.giusdp.htduels.duel;

import java.util.UUID;

public record DuelId(UUID value) {
    public static DuelId generate() { return new DuelId(UUID.randomUUID()); }
}
