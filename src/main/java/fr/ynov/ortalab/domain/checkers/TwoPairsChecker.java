package main.java.fr.ynov.ortalab.domain.checkers;

import main.java.fr.ynov.ortalab.domain.card.Card;
import main.java.fr.ynov.ortalab.domain.card.CardValue;
import main.java.fr.ynov.ortalab.domain.game.HandType;
import main.java.fr.ynov.ortalab.domain.utils.HandUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Checks for a Two Pair hand (two cards of one value, two cards of another value).
 * Also selects the highest remaining card as a kicker.
 */
public class TwoPairsChecker implements HandChecker {

    @Override
    public boolean checkHand(List<Card> cards, Set<Card> usedCards, Set<Card> coreCards) {
        List<CardValue> pairValues = HandUtils.findValueGroups(cards, 2);

        if (pairValues.size() < 2) {
            return false;
        }

        // Get two highest pairs
        CardValue firstPairValue = pairValues.get(0);
        CardValue secondPairValue = pairValues.get(1);

        // Add pairs to used and core cards
        List<Card> firstPairCards = cards.stream()
                .filter(card -> card.value() == firstPairValue)
                .limit(2)
                .toList();

        List<Card> secondPairCards = cards.stream()
                .filter(card -> card.value() == secondPairValue)
                .limit(2)
                .toList();

        usedCards.addAll(firstPairCards);
        usedCards.addAll(secondPairCards);
        coreCards.addAll(firstPairCards);
        coreCards.addAll(secondPairCards);

        // Find the highest kicker to complete the 5-card hand
        Set<CardValue> excludeValues = new HashSet<>();
        excludeValues.add(firstPairValue);
        excludeValues.add(secondPairValue);
        List<Card> kickers = HandUtils.getTopCardsByValue(cards, excludeValues, 1);

        if (!kickers.isEmpty()) {
            usedCards.add(kickers.getFirst());
        }

        return true;
    }

    @Override
    public HandType getHandType() {
        return HandType.TWO_PAIR;
    }
}