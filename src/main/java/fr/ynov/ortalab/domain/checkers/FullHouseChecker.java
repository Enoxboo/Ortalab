package main.java.fr.ynov.ortalab.domain.checkers;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.CardValue;
import main.java.fr.ynov.ortalab.domain.HandType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FullHouseChecker implements HandChecker {
    @Override
    public boolean checkHand(List<Card> cards, Set<Card> usedCards, Set<Card> coreCards) {
        Map<CardValue, List<Card>> valueGroups = cards.stream()
                .collect(Collectors.groupingBy(Card::getValue));

        List<CardValue> threeOfAKindValues = new ArrayList<>();
        List<CardValue> pairValues = new ArrayList<>();

        for (Map.Entry<CardValue, List<Card>> entry : valueGroups.entrySet()) {
            if (entry.getValue().size() >= 3) {
                threeOfAKindValues.add(entry.getKey());
            } else if (entry.getValue().size() >= 2) {
                pairValues.add(entry.getKey());
            }
        }

        if (threeOfAKindValues.isEmpty()) {
            return false;
        }

        threeOfAKindValues.sort((v1, v2) -> v2.getNumericValue() - v1.getNumericValue());

        if (threeOfAKindValues.size() > 1) {
            CardValue threesValue = threeOfAKindValues.get(0);
            CardValue pairValue = threeOfAKindValues.get(1);

            List<Card> threeCards = valueGroups.get(threesValue).subList(0, 3);
            List<Card> pairCards = valueGroups.get(pairValue).subList(0, 2);

            usedCards.addAll(threeCards);
            usedCards.addAll(pairCards);

            coreCards.addAll(threeCards);
            coreCards.addAll(pairCards);

            return true;
        }

        if (pairValues.isEmpty()) {
            return false;
        }

        pairValues.sort((v1, v2) -> v2.getNumericValue() - v1.getNumericValue());

        CardValue threesValue = threeOfAKindValues.getFirst();
        CardValue pairValue = pairValues.getFirst();

        List<Card> threeCards = valueGroups.get(threesValue).subList(0, 3);
        List<Card> pairCards = valueGroups.get(pairValue).subList(0, 2);

        usedCards.addAll(threeCards);
        usedCards.addAll(pairCards);

        coreCards.addAll(threeCards);
        coreCards.addAll(pairCards);

        return true;
    }

    @Override
    public HandType getHandType() {
        return HandType.FULL_HOUSE;
    }
}