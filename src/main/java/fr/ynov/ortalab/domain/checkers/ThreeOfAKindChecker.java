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

public class ThreeOfAKindChecker implements HandChecker {
    @Override
    public boolean checkHand(List<Card> cards, Set<Card> usedCards, Set<Card> coreCards) {
        // Use EnumMap for efficient grouping
        Map<CardValue, List<Card>> valueGroups = new EnumMap<>(CardValue.class);

        // Single pass grouping
        for (Card card : cards) {
            valueGroups.computeIfAbsent(card.getValue(), k -> new ArrayList<>()).add(card);
        }

        for (Map.Entry<CardValue, List<Card>> entry : valueGroups.entrySet()) {
            if (entry.getValue().size() == 3) {
                // Add three of a kind cards to both used and core cards
                List<Card> threeOfAKindCards = entry.getValue();
                usedCards.addAll(threeOfAKindCards);
                coreCards.addAll(threeOfAKindCards);

                // More efficient kicker selection
                List<Card> kickers = cards.stream()
                        .filter(card -> card.getValue() != entry.getKey())
                        .sorted(Comparator.comparingInt((Card c) -> c.getValue().getNumericValue()).reversed())
                        .limit(2)
                        .toList();

                usedCards.addAll(kickers);
                return true;
            }
        }

        return false;
    }

    @Override
    public HandType getHandType() {
        return HandType.THREE_OF_A_KIND;
    }
}