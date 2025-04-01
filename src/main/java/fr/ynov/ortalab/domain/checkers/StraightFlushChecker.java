package main.java.fr.ynov.ortalab.domain.checkers;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.CardSuit;
import main.java.fr.ynov.ortalab.domain.CardValue;
import main.java.fr.ynov.ortalab.domain.HandType;
import main.java.fr.ynov.ortalab.domain.utils.HandUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.Comparator;

public class StraightFlushChecker implements HandChecker {

    @Override
    public boolean checkHand(List<Card> cards, Set<Card> usedCards, Set<Card> coreCards) {
        Map<CardSuit, List<Card>> suitGroups = HandUtils.groupBySuit(cards);

        for (Map.Entry<CardSuit, List<Card>> entry : suitGroups.entrySet()) {
            List<Card> sameSuitCards = entry.getValue();

            if (sameSuitCards.size() < 5) {
                continue;
            }

            List<CardValue> distinctValues = sameSuitCards.stream()
                    .map(Card::getValue)
                    .distinct()
                    .sorted(Comparator.comparingInt(CardValue::getNumericValue))
                    .toList();

            // Check for wheel straight flush (A-5)
            if (distinctValues.contains(CardValue.TWO) &&
                    distinctValues.contains(CardValue.THREE) &&
                    distinctValues.contains(CardValue.FOUR) &&
                    distinctValues.contains(CardValue.FIVE) &&
                    distinctValues.contains(CardValue.ACE)) {

                List<Card> straightFlushCards = sameSuitCards.stream()
                        .filter(card -> card.getValue() == CardValue.ACE ||
                                card.getValue() == CardValue.TWO ||
                                card.getValue() == CardValue.THREE ||
                                card.getValue() == CardValue.FOUR ||
                                card.getValue() == CardValue.FIVE)
                        .limit(5)
                        .toList();

                usedCards.addAll(straightFlushCards);
                coreCards.addAll(straightFlushCards);
                return true;
            }

            // Check for regular straight flush
            for (int i = 0; i <= distinctValues.size() - 5; i++) {
                boolean isStraight = true;
                for (int j = i; j < i + 4; j++) {
                    if (distinctValues.get(j).getNumericValue() + 1 != distinctValues.get(j + 1).getNumericValue()) {
                        isStraight = false;
                        break;
                    }
                }

                if (isStraight) {
                    Set<CardValue> straightValues = new HashSet<>();
                    for (int j = i; j < i + 5; j++) {
                        straightValues.add(distinctValues.get(j));
                    }

                    List<Card> straightFlushCards = sameSuitCards.stream()
                            .filter(card -> straightValues.contains(card.getValue()))
                            .limit(5)
                            .toList();

                    usedCards.addAll(straightFlushCards);
                    coreCards.addAll(straightFlushCards);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public HandType getHandType() {
        return HandType.STRAIGHT_FLUSH;
    }
}