package com.giusdp.htduels.duel.event;

public record DrawCards(int playerIndex, int count) implements DuelEvent {}
