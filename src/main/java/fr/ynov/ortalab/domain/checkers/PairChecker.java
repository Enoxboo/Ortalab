package main.java.fr.ynov.ortalab.domain.checkers;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.CardValue;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PairChecker {
    public static boolean isPair(List<Card> cards, Set<Card> usedCards) {
        Map<CardValue, List<Card>> valueGroups = cards.stream()
                .collect(Collectors.groupingBy(Card::getValue));

        for (Map.Entry<CardValue, List<Card>> entry : valueGroups.entrySet()) {
            if (entry.getValue().size() == 2) {
                usedCards.addAll(entry.getValue());

                List<Card> kickers = cards.stream()
                        .filter(card -> card.getValue() != entry.getKey())
                        .sorted((c1, c2) -> c2.getValue().getNumericValue() - c1.getValue().getNumericValue())
                        .limit(3)
                        .toList();

                usedCards.addAll(kickers);
                return true;
            }
        }

        return false;
    }
}
