package main.java.fr.ynov.ortalab.domain.checkers;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.CardSuit;
import main.java.fr.ynov.ortalab.domain.HandType;
import main.java.fr.ynov.ortalab.domain.utils.HandUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class FlushChecker implements HandChecker {

    @Override
    public boolean checkHand(List<Card> cards, Set<Card> usedCards, Set<Card> coreCards) {
        Map<CardSuit, List<Card>> suitGroups = HandUtils.groupBySuit(cards);

        for (Map.Entry<CardSuit, List<Card>> entry : suitGroups.entrySet()) {
            if (entry.getValue().size() >= 5) {
                List<Card> flushCards = entry.getValue().stream()
                        .sorted((c1, c2) -> c2.getValue().getNumericValue() - c1.getValue().getNumericValue())
                        .limit(5)
                        .toList();

                usedCards.addAll(flushCards);
                coreCards.addAll(flushCards);
                return true;
            }
        }

        return false;
    }

    @Override
    public HandType getHandType() {
        return HandType.FLUSH;
    }
}