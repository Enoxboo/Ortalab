package main.java.fr.ynov.ortalab.domain.checkers;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.CardValue;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class StraightChecker {

    public static boolean isStraight(List<Card> cards, Set<Card> usedCards) {
        List<CardValue> distinctValues = cards.stream()
                .map(Card::getValue)
                .distinct()
                .sorted(Comparator.comparingInt(CardValue::getNumericValue))
                .toList();

        if (distinctValues.contains(CardValue.TWO) &&
                distinctValues.contains(CardValue.THREE) &&
                distinctValues.contains(CardValue.FOUR) &&
                distinctValues.contains(CardValue.FIVE) &&
                distinctValues.contains(CardValue.ACE)) {

            Card aceCard = findCardWithValue(cards, CardValue.ACE);
            Card twoCard = findCardWithValue(cards, CardValue.TWO);
            Card threeCard = findCardWithValue(cards, CardValue.THREE);
            Card fourCard = findCardWithValue(cards, CardValue.FOUR);
            Card fiveCard = findCardWithValue(cards, CardValue.FIVE);

            usedCards.add(aceCard);
            usedCards.add(twoCard);
            usedCards.add(threeCard);
            usedCards.add(fourCard);
            usedCards.add(fiveCard);

            return true;
        }

        for (int i = 0; i <= distinctValues.size() - 5; i++) {
            boolean isStraight = true;
            for (int j = i; j < i + 4; j++) {
                if (distinctValues.get(j).getNumericValue() + 1 !=
                        distinctValues.get(j + 1).getNumericValue()) {
                    isStraight = false;
                    break;
                }
            }

            if (isStraight) {
                for (int j = i; j < i + 5; j++) {
                    CardValue requiredValue = distinctValues.get(j);
                    Card card = findCardWithValue(cards, requiredValue);
                    usedCards.add(card);
                }
                return true;
            }
        }

        return false;
    }

    private static Card findCardWithValue(List<Card> cards, CardValue value) {
        return cards.stream()
                .filter(card -> card.getValue() == value)
                .findFirst()
                .orElse(null);
    }
}
