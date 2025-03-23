package main.java.fr.ynov.ortalab.domain.checkers;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.CardValue;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ThreeOfAKindChecker {
    public static boolean isThreeOfAKind(List<Card> cards, Set<Card> usedCards) {
        Map<CardValue, List<Card>> valueGroups = cards.stream()
                .collect(Collectors.groupingBy(Card::getValue));

        for (Map.Entry<CardValue, List<Card>> entry : valueGroups.entrySet()) {
            if (entry.getValue().size() == 3) {
                usedCards.addAll(entry.getValue());

                List<Card> kickers = cards.stream()
                        .filter(card -> card.getValue() != entry.getKey())
                        .sorted((c1, c2) -> c2.getValue().getNumericValue() - c1.getValue().getNumericValue())
                        .limit(2)
                        .toList();

                usedCards.addAll(kickers);
                return true;
            }
        }

        return false;
    }


    public static void identifyThreeOfAKindCoreCards(Set<Card> usedCards, Set<Card> coreCards) {
        // Group the used cards by value
        CardValue threeOfAKindValue = null;
        for (Card card : usedCards) {
            int sameValueCount = 0;
            for (Card other : usedCards) {
                if (card.getValue() == other.getValue()) {
                    sameValueCount++;
                }
            }
            if (sameValueCount == 3) {
                threeOfAKindValue = card.getValue();
                break;
            }
        }

        // Add only the three matching cards to the core cards
        if (threeOfAKindValue != null) {
            for (Card card : usedCards) {
                if (card.getValue() == threeOfAKindValue) {
                    coreCards.add(card);
                }
            }
        }
    }
}
