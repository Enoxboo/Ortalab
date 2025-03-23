package main.java.fr.ynov.ortalab.domain.checkers;

import main.java.fr.ynov.ortalab.domain.Card;

import java.util.List;
import java.util.Set;

public class HighCardChecker {
    public static void isHighCard(List<Card> cards, Set<Card> usedCards) {
        if (cards.isEmpty()) {
            return;
        }

        Card highestCard = cards.stream()
                .max((c1, c2) -> c1.getValue().getNumericValue() - c2.getValue().getNumericValue())
                .orElse(null);

        if (highestCard != null) {
            usedCards.add(highestCard);
        }
    }
}
