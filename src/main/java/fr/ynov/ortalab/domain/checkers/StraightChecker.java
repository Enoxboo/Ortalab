package main.java.fr.ynov.ortalab.domain.checkers;

import main.java.fr.ynov.ortalab.domain.card.Card;
import main.java.fr.ynov.ortalab.domain.HandType;
import main.java.fr.ynov.ortalab.domain.utils.HandUtils;

import java.util.List;
import java.util.Set;

public class StraightChecker implements HandChecker {

    @Override
    public boolean checkHand(List<Card> cards, Set<Card> usedCards, Set<Card> coreCards) {
        List<Card> straightCards = HandUtils.getStraightCards(cards);

        if (straightCards.isEmpty()) {
            return false;
        }

        usedCards.addAll(straightCards);
        coreCards.addAll(straightCards);
        return true;
    }

    @Override
    public HandType getHandType() {
        return HandType.STRAIGHT;
    }
}