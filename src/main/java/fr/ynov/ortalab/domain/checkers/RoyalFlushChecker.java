package main.java.fr.ynov.ortalab.domain.checkers;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.CardSuit;
import main.java.fr.ynov.ortalab.domain.CardValue;
import main.java.fr.ynov.ortalab.domain.utils.HandUtils;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RoyalFlushChecker {
    public static boolean isRoyalFlush(List<Card> cards, Set<Card> usedCards) {
        List<Card> sortedCards = HandUtils.getSortedCards(cards);

        CardSuit suit = sortedCards.getFirst().getSuit();
        boolean isFlush = sortedCards.stream().allMatch(card -> card.getSuit() == suit);

        if (!isFlush) {
            return false;
        }

        boolean isRoyal = false;
        List<Card> royalCards = new ArrayList<>();

        for (Card card : sortedCards) {
            if (card.getValue() == CardValue.TEN ||
                    card.getValue() == CardValue.JACK ||
                    card.getValue() == CardValue.QUEEN ||
                    card.getValue() == CardValue.KING ||
                    card.getValue() == CardValue.ACE) {
                royalCards.add(card);
            }
        }

        if (royalCards.size() >= 5) {
            Set<CardValue> values = royalCards.stream()
                    .map(Card::getValue)
                    .collect(Collectors.toSet());

            isRoyal = values.contains(CardValue.TEN) &&
                    values.contains(CardValue.JACK) &&
                    values.contains(CardValue.QUEEN) &&
                    values.contains(CardValue.KING) &&
                    values.contains(CardValue.ACE);

            if (isRoyal) {
                for (Card card : royalCards) {
                    if (card.getValue() == CardValue.TEN ||
                            card.getValue() == CardValue.JACK ||
                            card.getValue() == CardValue.QUEEN ||
                            card.getValue() == CardValue.KING ||
                            card.getValue() == CardValue.ACE) {
                        usedCards.add(card);
                        if (usedCards.size() == 5) break;
                    }
                }
            }
        }

        return isRoyal;
    }
}
