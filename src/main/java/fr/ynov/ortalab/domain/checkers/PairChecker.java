package main.java.fr.ynov.ortalab.domain.checkers;

import main.java.fr.ynov.ortalab.domain.card.Card;
import main.java.fr.ynov.ortalab.domain.card.CardValue;
import main.java.fr.ynov.ortalab.domain.HandType;
import main.java.fr.ynov.ortalab.domain.utils.HandUtils;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * Checks for a "Pair" hand (two cards of the same value).
 * Also selects the three highest remaining cards as kickers.
 */
public class PairChecker implements HandChecker {

    @Override
    public boolean checkHand(List<Card> cards, Set<Card> usedCards, Set<Card> coreCards) {
        List<CardValue> pairValues = HandUtils.findValueGroups(cards, 2);

        if (pairValues.isEmpty()) {
            return false;
        }

        // Get the highest pair
        CardValue pairValue = pairValues.getFirst();
        List<Card> pairCards = cards.stream()
                .filter(card -> card.value() == pairValue)
                .limit(2)
                .toList();

        usedCards.addAll(pairCards);
        coreCards.addAll(pairCards);

        // Find three highest kickers to complete the 5-card hand
        Set<CardValue> excludeValues = new HashSet<>();
        excludeValues.add(pairValue);
        List<Card> kickers = HandUtils.getTopCardsByValue(cards, excludeValues, 3);

        usedCards.addAll(kickers);

        return true;
    }

    @Override
    public HandType getHandType() {
        return HandType.PAIR;
    }
}