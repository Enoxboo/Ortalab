package main.java.fr.ynov.ortalab.domain.checkers;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.CardValue;
import main.java.fr.ynov.ortalab.domain.HandType;
import main.java.fr.ynov.ortalab.domain.utils.HandUtils;

import java.util.List;
import java.util.Set;

public class FullHouseChecker implements HandChecker {

    @Override
    public boolean checkHand(List<Card> cards, Set<Card> usedCards, Set<Card> coreCards) {
        List<CardValue> threesValues = HandUtils.findValueGroups(cards, 3);
        List<CardValue> pairsValues = HandUtils.findValueGroups(cards, 2);

        // Case 1: At least one three of a kind
        if (threesValues.isEmpty()) {
            return false;
        }

        // Get the highest three of a kind
        CardValue bestThreeValue = threesValues.get(0);
        List<Card> threeCards = cards.stream()
                .filter(card -> card.value() == bestThreeValue)
                .limit(3)
                .toList();

        // Case 2: Multiple three of a kinds (for potential items)
        if (threesValues.size() > 1) {
            CardValue secondThreeValue = threesValues.get(1);
            List<Card> pairFromThree = cards.stream()
                    .filter(card -> card.value() == secondThreeValue)
                    .limit(2)
                    .toList();

            usedCards.addAll(threeCards);
            usedCards.addAll(pairFromThree);
            coreCards.addAll(threeCards);
            coreCards.addAll(pairFromThree);
            return true;
        }

        // Case 3: Three of a kind + at least one pair
        if (!pairsValues.isEmpty()) {
            CardValue bestPairValue = pairsValues.getFirst();
            List<Card> pairCards = cards.stream()
                    .filter(card -> card.value() == bestPairValue)
                    .limit(2)
                    .toList();

            usedCards.addAll(threeCards);
            usedCards.addAll(pairCards);
            coreCards.addAll(threeCards);
            coreCards.addAll(pairCards);
            return true;
        }

        return false;
    }

    @Override
    public HandType getHandType() {
        return HandType.FULL_HOUSE;
    }
}