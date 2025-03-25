package main.java.fr.ynov.ortalab.domain.utils;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.CardSuit;
import main.java.fr.ynov.ortalab.domain.CardValue;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CardSorter {
    /**
     * Sorts cards by their numeric value in ascending order
     * @param cards List of cards to sort
     * @return Sorted list of cards
     */
    public static List<Card> sortByValue(List<Card> cards) {
        return cards.stream()
                .sorted(Comparator.comparingInt(card -> card.getValue().getNumericValue()))
                .collect(Collectors.toList());
    }

    /**
     * Sorts cards first by suit (Clubs, Hearts, Diamonds, Spades)
     * and then by their numeric value within each suit
     * @param cards List of cards to sort
     * @return Sorted list of cards
     */
    public static List<Card> sortBySuitAndValue(List<Card> cards) {
        return cards.stream()
                .sorted(Comparator
                        .comparing(Card::getSuit)
                        .thenComparingInt(card -> card.getValue().getNumericValue()))
                .collect(Collectors.toList());
    }
}