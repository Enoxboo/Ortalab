package main.java.fr.ynov.ortalab.domain.checkers;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.HandType;
import main.java.fr.ynov.ortalab.domain.utils.HandUtils;

import java.util.List;
import java.util.Set;

public class HighCardChecker implements HandChecker {

    @Override
    public boolean checkHand(List<Card> cards, Set<Card> usedCards, Set<Card> coreCards) {
        if (cards.isEmpty()) {
            return false;
        }

        // Get top 5 cards (or all if less than 5)
        List<Card> topCards = HandUtils.getSortedCards(cards);
        List<Card> bestFiveCards = topCards.stream()
                .limit(5)
                .toList();

        if (!bestFiveCards.isEmpty()) {
            Card highestCard = bestFiveCards.getFirst();
            coreCards.add(highestCard);
            usedCards.addAll(bestFiveCards);
            return true;
        }

        return false;
    }

    @Override
    public HandType getHandType() {
        return HandType.HIGH_CARD;
    }
}