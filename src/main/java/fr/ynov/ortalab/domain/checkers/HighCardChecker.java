package main.java.fr.ynov.ortalab.domain.checkers;

import main.java.fr.ynov.ortalab.domain.card.Card;
import main.java.fr.ynov.ortalab.domain.game.HandType;
import main.java.fr.ynov.ortalab.domain.utils.HandUtils;

import java.util.List;
import java.util.Set;

/**
 * Checks for a High Card hand (no other hand type was found).
 * Takes the five highest cards by value.
 */
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
            coreCards.add(highestCard);  // Only the highest card is considered "core"
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