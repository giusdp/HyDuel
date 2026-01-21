package com.giusdp.htduels.duel.handlers;

import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.event.DuelEvent;
import com.giusdp.htduels.duel.event.PlayCard;
import org.jspecify.annotations.NonNull;

public class PlayCardHandler extends DuelEventHandler {
  public PlayCardHandler(@NonNull Duel duel) {
    super(duel);
  }

  @Override
  public void accept(DuelEvent ev) {
    PlayCard playCard = (PlayCard) ev;

    assert playCard.duelist().removeFromHand(playCard.card());
    duel.battlefield.placeCard(playCard.duelist(), playCard.card());
  }
}
