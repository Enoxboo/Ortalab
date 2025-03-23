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

    public static void identifyFourOfAKindCoreCards(Set<Card> usedCards, Set<Card> coreCards) {
        // Group the used cards by value
        CardValue fourOfAKindValue = null;
        for (Card card : usedCards) {
            int sameValueCount = 0;
            for (Card other : usedCards) {
                if (card.getValue() == other.getValue()) {
                    sameValueCount++;
                }
            }
            if (sameValueCount == 4) {
                fourOfAKindValue = card.getValue();
                break;
            }
        }

        // Add only the four matching cards to the core cards
        if (fourOfAKindValue != null) {
            for (Card card : usedCards) {
                if (card.getValue() == fourOfAKindValue) {
                    coreCards.add(card);
                }
            }
        }
    }

}
