package main.java.fr.ynov.ortalab.domain.checkers;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.CardSuit;
import main.java.fr.ynov.ortalab.domain.CardValue;
import main.java.fr.ynov.ortalab.domain.HandType;
import main.java.fr.ynov.ortalab.domain.utils.HandUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class StraightFlushChecker implements HandChecker {
    @Override
    public boolean checkHand(List<Card> cards, Set<Card> usedCards, Set<Card> coreCards) {
        List<Card> sortedCards = HandUtils.getSortedCards(cards);

        Map<CardSuit, List<Card>> suitGroups = cards.stream()
                .collect(Collectors.groupingBy(Card::getSuit));

        for (List<Card> suitGroup : suitGroups.values()) {
            if (suitGroup.size() >= 5) {
                List<Card> suitSorted = suitGroup.stream()
                        .sorted(Comparator.comparingInt(card -> card.getValue().getNumericValue()))
                        .toList();

                for (int i = 0; i <= suitSorted.size() - 5; i++) {
                    boolean isStraight = true;
                    for (int j = i; j < i + 4; j++) {
                        if (suitSorted.get(j).getValue().getNumericValue() + 1 !=
                                suitSorted.get(j + 1).getValue().getNumericValue()) {
                            isStraight = false;
                            break;
                        }
                    }

                    if (isStraight) {
                        List<Card> straightFlushCards = suitSorted.subList(i, i + 5);
                        usedCards.addAll(straightFlushCards);
                        coreCards.addAll(straightFlushCards);
                        return true;
                    }
                }

                boolean hasAce = suitGroup.stream().anyMatch(card -> card.getValue() == CardValue.ACE);
                boolean hasTwo = suitGroup.stream().anyMatch(card -> card.getValue() == CardValue.TWO);
                boolean hasThree = suitGroup.stream().anyMatch(card -> card.getValue() == CardValue.THREE);
                boolean hasFour = suitGroup.stream().anyMatch(card -> card.getValue() == CardValue.FOUR);
                boolean hasFive = suitGroup.stream().anyMatch(card -> card.getValue() == CardValue.FIVE);

                if (hasAce && hasTwo && hasThree && hasFour && hasFive) {
                    List<Card> lowStraightFlushCards = new java.util.ArrayList<>();
                    for (Card card : suitGroup) {
                        if (card.getValue() == CardValue.ACE ||
                                card.getValue() == CardValue.TWO ||
                                card.getValue() == CardValue.THREE ||
                                card.getValue() == CardValue.FOUR ||
                                card.getValue() == CardValue.FIVE) {
                            lowStraightFlushCards.add(card);
                            usedCards.add(card);
                        }
                    }
                    coreCards.addAll(lowStraightFlushCards);
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