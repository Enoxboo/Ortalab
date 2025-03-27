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

public class PairChecker implements HandChecker {
    @Override
    public boolean checkHand(List<Card> cards, Set<Card> usedCards, Set<Card> coreCards) {
        // Use EnumMap for potentially faster lookup
        Map<CardValue, List<Card>> valueGroups = new EnumMap<>(CardValue.class);

        // Single pass grouping
        for (Card card : cards) {
            valueGroups.computeIfAbsent(card.getValue(), k -> new ArrayList<>()).add(card);
        }

        for (Map.Entry<CardValue, List<Card>> entry : valueGroups.entrySet()) {
            if (entry.getValue().size() == 2) {
                // Add the pair cards to both used and core cards
                List<Card> pairCards = entry.getValue();
                usedCards.addAll(pairCards);
                coreCards.addAll(pairCards);

                // More efficient kicker selection
                List<Card> kickers = cards.stream()
                        .filter(card -> card.getValue() != entry.getKey())
                        .sorted(Comparator.comparingInt((Card c) -> c.getValue().getNumericValue()).reversed())
                        .limit(3)
                        .toList();

                usedCards.addAll(kickers);
                return true;
            }
        }

        return false;
    }

    @Override
    public HandType getHandType() {
        return HandType.PAIR;
    }
}