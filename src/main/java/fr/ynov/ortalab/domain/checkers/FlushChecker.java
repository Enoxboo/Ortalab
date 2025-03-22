package main.java.fr.ynov.ortalab.domain.checkers;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.CardSuit;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FlushChecker {
    public static boolean isFlush(List<Card> cards, Set<Card> usedCards) {
        Map<CardSuit, List<Card>> suitGroups = cards.stream()
                .collect(Collectors.groupingBy(Card::getSuit));

        for (Map.Entry<CardSuit, List<Card>> entry : suitGroups.entrySet()) {
            if (entry.getValue().size() >= 5) {
                List<Card> flushCards = entry.getValue().stream()
                        .sorted((c1, c2) -> c2.getValue().getNumericValue() - c1.getValue().getNumericValue())
                        .toList();

                usedCards.addAll(flushCards.subList(0, 5));
                return true;
            }
        }

        return false;
    }
}
