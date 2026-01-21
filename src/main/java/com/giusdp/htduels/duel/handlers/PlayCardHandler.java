package com.giusdp.htduels.duel.handlers;

import com.giusdp.htduels.duel.Duel;
import com.giusdp.htduels.duel.event.DuelEvent;
import com.giusdp.htduels.duel.event.PlayCard;
import org.jspecify.annotations.NonNull;

public class PlayCardHandler extends DuelEventHandler {
  @Override
  public void accept(DuelEvent ev) {
    PlayCard playCard = (PlayCard) ev;
    System.out.println("[Duel] Player " + playCard.duelist + " plays card " + playCard.card.name());

    System.out.println("[Duel] Removing card from hand: " + playCard.duelist.getHand().size());
    playCard.duelist.removeFromHand(playCard.card);
    System.out.println("[Duel] Hand size after removal: " + playCard.duelist.getHand().size());

    ev.duel.battlefield.placeCard(playCard.duelist, playCard.card);
  }
}
