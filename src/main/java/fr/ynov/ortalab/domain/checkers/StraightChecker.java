package main.java.fr.ynov.ortalab.domain.checkers;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.CardValue;
import main.java.fr.ynov.ortalab.domain.HandType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class StraightChecker implements HandChecker {
    @Override
    public boolean checkHand(List<Card> cards, Set<Card> usedCards, Set<Card> coreCards) {
        List<CardValue> distinctValues = cards.stream()
                .map(Card::getValue)
                .distinct()
                .sorted(Comparator.comparingInt(CardValue::getNumericValue))
                .toList();

        // Check for A-5 straight (Ace low)
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

            List<Card> straightCards = new ArrayList<>();
            straightCards.add(aceCard);
            straightCards.add(twoCard);
            straightCards.add(threeCard);
            straightCards.add(fourCard);
            straightCards.add(fiveCard);

            usedCards.addAll(straightCards);
            coreCards.addAll(straightCards);

            return true;
        }

        // Check for normal straights
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
                List<Card> straightCards = new ArrayList<>();
                for (int j = i; j < i + 5; j++) {
                    CardValue requiredValue = distinctValues.get(j);
                    Card card = findCardWithValue(cards, requiredValue);
                    straightCards.add(card);
                }

                usedCards.addAll(straightCards);
                coreCards.addAll(straightCards);
                return true;
            }
        }

        return false;
    }

    private Card findCardWithValue(List<Card> cards, CardValue value) {
        return cards.stream()
                .filter(card -> card.getValue() == value)
                .findFirst()
                .orElse(null);
    }

    @Override
    public HandType getHandType() {
        return HandType.STRAIGHT;
    }
}