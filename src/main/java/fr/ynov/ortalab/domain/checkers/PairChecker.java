package main.java.fr.ynov.ortalab.domain.checkers;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.CardValue;
import main.java.fr.ynov.ortalab.domain.HandType;
import main.java.fr.ynov.ortalab.domain.utils.HandUtils;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class PairChecker implements HandChecker {

    @Override
    public boolean checkHand(List<Card> cards, Set<Card> usedCards, Set<Card> coreCards) {
        List<CardValue> pairValues = HandUtils.findValueGroups(cards, 2);

        if (pairValues.isEmpty()) {
            return false;
        }

        // Get highest pair
        CardValue pairValue = pairValues.getFirst();
        List<Card> pairCards = cards.stream()
                .filter(card -> card.getValue() == pairValue)
                .limit(2)
                .toList();

        usedCards.addAll(pairCards);
        coreCards.addAll(pairCards);

        // Find three highest kickers
        Set<CardValue> excludeValues = new HashSet<>();
        excludeValues.add(pairValue);
        List<Card> kickers = HandUtils.getTopCardsByValue(cards, excludeValues, 3);

        usedCards.addAll(kickers);

        return true;
    }

    @Override
    public HandType getHandType() {
        return HandType.PAIR;
    }
}