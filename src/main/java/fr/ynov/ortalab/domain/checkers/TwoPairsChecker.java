package main.java.fr.ynov.ortalab.domain.checkers;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.CardValue;
import main.java.fr.ynov.ortalab.domain.HandType;

import java.util.*;
import java.util.stream.Collectors;

public class TwoPairsChecker implements HandChecker {
    @Override
    public boolean checkHand(List<Card> cards, Set<Card> usedCards, Set<Card> coreCards) {
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

        List<Card> firstPairCards = valueGroups.get(firstPairValue).subList(0, 2);
        List<Card> secondPairCards = valueGroups.get(secondPairValue).subList(0, 2);

        // Add the pairs to both used and core cards
        usedCards.addAll(firstPairCards);
        usedCards.addAll(secondPairCards);

        coreCards.addAll(firstPairCards);
        coreCards.addAll(secondPairCards);

        // Add the kicker to used cards only
        cards.stream()
                .filter(card -> card.getValue() != firstPairValue && card.getValue() != secondPairValue)
                .max(Comparator.comparingInt(card -> card.getValue().getNumericValue()))
                .ifPresent(usedCards::add);

        return true;
    }

    @Override
    public HandType getHandType() {
        return HandType.TWO_PAIR;
    }
}