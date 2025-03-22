package main.java.fr.ynov.ortalab.domain.checkers;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.CardValue;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FourOfAKindChecker {
    public static boolean isFourOfAKind(List<Card> cards, Set<Card> usedCards) {
        Map<CardValue, List<Card>> valueGroups = cards.stream()
                .collect(Collectors.groupingBy(Card::getValue));

        for (Map.Entry<CardValue, List<Card>> entry : valueGroups.entrySet()) {
            if (entry.getValue().size() == 4) {
                usedCards.addAll(entry.getValue());

                cards.stream()
                        .filter(card -> card.getValue() != entry.getKey())
                        .max(Comparator.comparingInt(card -> card.getValue().getNumericValue())).ifPresent(usedCards::add);

                return true;
            }
        }

        return false;
    }
}
