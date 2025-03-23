package main.java.fr.ynov.ortalab.domain.checkers;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.CardValue;
import main.java.fr.ynov.ortalab.domain.HandType;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FourOfAKindChecker implements HandChecker {
    @Override
    public boolean checkHand(List<Card> cards, Set<Card> usedCards, Set<Card> coreCards) {
        Map<CardValue, List<Card>> valueGroups = cards.stream()
                .collect(Collectors.groupingBy(Card::getValue));

        for (Map.Entry<CardValue, List<Card>> entry : valueGroups.entrySet()) {
            if (entry.getValue().size() == 4) {
                // Add the four cards to both usedCards and coreCards
                usedCards.addAll(entry.getValue());
                coreCards.addAll(entry.getValue());

                // Add the highest kicker to usedCards only
                cards.stream()
                        .filter(card -> card.getValue() != entry.getKey())
                        .max(Comparator.comparingInt(card -> card.getValue().getNumericValue()))
                        .ifPresent(usedCards::add);

                return true;
            }
        }

        return false;
    }

    @Override
    public HandType getHandType() {
        return HandType.FOUR_OF_A_KIND;
    }
}