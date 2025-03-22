package main.java.fr.ynov.ortalab.domain.checkers;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.CardValue;

import java.util.*;
import java.util.stream.Collectors;

public class TwoPairsChecker {
    public static boolean isTwoPair(List<Card> cards, Set<Card> usedCards) {
        Map<CardValue, List<Card>> valueGroups = cards.stream()
                .collect(Collectors.groupingBy(Card::getValue));

        List<CardValue> pairValues = new ArrayList<>();

        for (Map.Entry<CardValue, List<Card>> entry : valueGroups.entrySet()) {
            if (entry.getValue().size() >= 2) {
                pairValues.add(entry.getKey());
            }
        }

        if (pairValues.size() < 2) {
            return false;
        }

        pairValues.sort((v1, v2) -> v2.getNumericValue() - v1.getNumericValue());

        CardValue firstPairValue = pairValues.get(0);
        CardValue secondPairValue = pairValues.get(1);

        usedCards.addAll(valueGroups.get(firstPairValue).subList(0, 2));

        usedCards.addAll(valueGroups.get(secondPairValue).subList(0, 2));

        cards.stream()
                .filter(card -> card.getValue() != firstPairValue && card.getValue() != secondPairValue)
                .max(Comparator.comparingInt(card -> card.getValue().getNumericValue())).ifPresent(usedCards::add);

        return true;
    }
}
