package com.giusdp.htduels.match.deck;

import java.util.List;

public class DeckRules {
    public static final int MAX_COPIES_PER_CARD = 2;
    public static final int REQUIRED_CARD_COUNT = 20;

    public boolean canAddCard(List<String> currentCardIds, String cardIdToAdd) {
        long copies = currentCardIds.stream()
                .filter(id -> id.equals(cardIdToAdd))
                .count();
        return copies < MAX_COPIES_PER_CARD;
    }

    public boolean isValidForDuel(int cardCount) {
        return cardCount == REQUIRED_CARD_COUNT;
    }

    public int cardsNeeded(int currentCount) {
        return Math.max(0, REQUIRED_CARD_COUNT - currentCount);
    }
}
