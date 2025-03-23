package main.java.fr.ynov.ortalab.domain.checkers;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.CardValue;
import main.java.fr.ynov.ortalab.domain.HandType;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PairChecker implements HandChecker {
    @Override
    public boolean checkHand(List<Card> cards, Set<Card> usedCards, Set<Card> coreCards) {
        Map<CardValue, List<Card>> valueGroups = cards.stream()
                .collect(Collectors.groupingBy(Card::getValue));

        for (Map.Entry<CardValue, List<Card>> entry : valueGroups.entrySet()) {
            if (entry.getValue().size() == 2) {
                // Add the pair cards to both used and core cards
                usedCards.addAll(entry.getValue());
                coreCards.addAll(entry.getValue());

                // Add kickers to used cards only
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

    @Override
    public HandType getHandType() {
        return HandType.PAIR;
    }
}