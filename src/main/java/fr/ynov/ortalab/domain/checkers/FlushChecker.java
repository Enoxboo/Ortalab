package main.java.fr.ynov.ortalab.domain.checkers;

import main.java.fr.ynov.ortalab.domain.card.Card;
import main.java.fr.ynov.ortalab.domain.card.CardSuit;
import main.java.fr.ynov.ortalab.domain.game.HandType;
import main.java.fr.ynov.ortalab.domain.utils.HandUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Checks for a Flush hand (five cards of the same suit).
 * Takes the highest five cards of the same suit.
 */
public class FlushChecker implements HandChecker {

    @Override
    public boolean checkHand(List<Card> cards, Set<Card> usedCards, Set<Card> coreCards) {
        // Group cards by suit
        Map<CardSuit, List<Card>> suitGroups = HandUtils.groupBySuit(cards);

        // Check if any suit has at least 5 cards
        for (Map.Entry<CardSuit, List<Card>> entry : suitGroups.entrySet()) {
            if (entry.getValue().size() >= 5) {
                // Take the 5 highest cards of that suit
                List<Card> flushCards = entry.getValue().stream()
                        .sorted((c1, c2) -> c2.value().getNumericValue() - c1.value().getNumericValue())
                        .limit(5)
                        .toList();

                usedCards.addAll(flushCards);
                coreCards.addAll(flushCards);
                return true;
            }
        }

        return false;
    }

    @Override
    public HandType getHandType() {
        return HandType.FLUSH;
    }
}