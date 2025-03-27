package main.java.fr.ynov.ortalab.domain.checkers;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.CardValue;
import main.java.fr.ynov.ortalab.domain.HandType;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.Comparator;


public class TwoPairsChecker implements HandChecker {
    @Override
    public boolean checkHand(List<Card> cards, Set<Card> usedCards, Set<Card> coreCards) {
        Map<CardValue, List<Card>> valueGroups = new EnumMap<>(CardValue.class);

        // Single pass grouping
        for (Card card : cards) {
            valueGroups.computeIfAbsent(card.getValue(), k -> new ArrayList<>()).add(card);
        }

        // Collect pair values efficiently
        List<CardValue> pairValues = valueGroups.entrySet().stream()
                .filter(entry -> entry.getValue().size() >= 2)
                .map(Map.Entry::getKey)
                .sorted(Comparator.comparingInt(CardValue::getNumericValue).reversed())
                .toList();

        // Ensure we have at least two pairs
        if (pairValues.size() < 2) {
            return false;
        }

        // Select the top two pair values
        CardValue firstPairValue = pairValues.get(0);
        CardValue secondPairValue = pairValues.get(1);

        // Get the first two cards of each pair
        List<Card> firstPairCards = valueGroups.get(firstPairValue).subList(0, 2);
        List<Card> secondPairCards = valueGroups.get(secondPairValue).subList(0, 2);

        // Add pairs to used and core cards
        usedCards.addAll(firstPairCards);
        usedCards.addAll(secondPairCards);
        coreCards.addAll(firstPairCards);
        coreCards.addAll(secondPairCards);

        Card kicker = cards.stream()
                .filter(card -> card.getValue() != firstPairValue && card.getValue() != secondPairValue)
                .max(Comparator.comparingInt(card -> card.getValue().getNumericValue()))
                .orElse(null);

        if (kicker != null) {
            usedCards.add(kicker);
        }

        return true;
    }

    @Override
    public HandType getHandType() {
        return HandType.TWO_PAIR;
    }
}