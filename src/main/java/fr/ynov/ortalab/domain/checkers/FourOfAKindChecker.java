package main.java.fr.ynov.ortalab.domain.checkers;

import main.java.fr.ynov.ortalab.domain.card.Card;
import main.java.fr.ynov.ortalab.domain.card.CardValue;
import main.java.fr.ynov.ortalab.domain.HandType;
import main.java.fr.ynov.ortalab.domain.utils.HandUtils;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class FourOfAKindChecker implements HandChecker {

    @Override
    public boolean checkHand(List<Card> cards, Set<Card> usedCards, Set<Card> coreCards) {
        List<CardValue> fourOfAKindValues = HandUtils.findValueGroups(cards, 4);

        if (fourOfAKindValues.isEmpty()) {
            return false;
        }

        CardValue quadsValue = fourOfAKindValues.getFirst();
        List<Card> quadsCards = cards.stream()
                .filter(card -> card.value() == quadsValue)
                .limit(4)
                .toList();

        usedCards.addAll(quadsCards);
        coreCards.addAll(quadsCards);

        // Find highest kicker
        Set<CardValue> excludeValues = new HashSet<>();
        excludeValues.add(quadsValue);
        List<Card> kickers = HandUtils.getTopCardsByValue(cards, excludeValues, 1);

        if (!kickers.isEmpty()) {
            usedCards.add(kickers.getFirst());
        }

        return true;
    }

    @Override
    public HandType getHandType() {
        return HandType.FOUR_OF_A_KIND;
    }
}