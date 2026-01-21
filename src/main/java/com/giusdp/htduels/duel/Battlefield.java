package com.giusdp.htduels.duel;

import com.giusdp.htduels.asset.CardAsset;
import com.giusdp.htduels.duelist.Duelist;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Battlefield {
  public final Map<Duelist, List<CardAsset>> sides;

  public Battlefield(Duelist[] duelists) {
    sides = new HashMap<>(2);
    for (Duelist duelist : duelists) {
      sides.put(duelist, new ArrayList<>());
    }
  }

  public void placeCard(Duelist duelist, CardAsset card) {
    assert sides.containsKey(duelist);
    sides.get(duelist).add(card);
  }

  public List<CardAsset> getSide(Duelist duelist) {
    return sides.get(duelist);
  }
}
