package main.java.fr.ynov.ortalab.domain.checkers;

import main.java.fr.ynov.ortalab.domain.card.Card;
import main.java.fr.ynov.ortalab.domain.card.CardValue;
import main.java.fr.ynov.ortalab.domain.HandType;
import main.java.fr.ynov.ortalab.domain.utils.HandUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Checks for a Three of a Kind hand (three cards of the same value).
 * Also selects the two highest remaining cards as kickers.
 */
public class ThreeOfAKindChecker implements HandChecker {

    @Override
    public boolean checkHand(List<Card> cards, Set<Card> usedCards, Set<Card> coreCards) {
        List<CardValue> threeOfAKindValues = HandUtils.findValueGroups(cards, 3);

        if (threeOfAKindValues.isEmpty()) {
            return false;
        }

        // Get the highest three of a kind
        CardValue tripValue = threeOfAKindValues.getFirst();
        List<Card> tripCards = cards.stream()
                .filter(card -> card.value() == tripValue)
                .limit(3)
                .toList();

        usedCards.addAll(tripCards);
        coreCards.addAll(tripCards);

        // Find two highest kickers to complete the 5-card hand
        Set<CardValue> excludeValues = new HashSet<>();
        excludeValues.add(tripValue);
        List<Card> kickers = HandUtils.getTopCardsByValue(cards, excludeValues, 2);

        usedCards.addAll(kickers);

        return true;
    }

    @Override
    public HandType getHandType() {
        return HandType.THREE_OF_A_KIND;
    }
}