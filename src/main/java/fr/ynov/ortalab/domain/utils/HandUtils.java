package main.java.fr.ynov.ortalab.domain.utils;

import main.java.fr.ynov.ortalab.domain.card.Card;
import main.java.fr.ynov.ortalab.domain.card.CardSuit;
import main.java.fr.ynov.ortalab.domain.card.CardValue;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Comparator;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;


/**
 * Utility class for poker hand evaluation operations
 */
public class HandUtils {

    /**
     * Groups cards by their value
     *
     * @param cards List of cards to group
     * @return Map of card values to lists of cards with that value
     */
    public static Map<CardValue, List<Card>> groupByValue(List<Card> cards) {
        return cards.stream()
                .collect(Collectors.groupingBy(Card::value));
    }

    /**
     * Groups cards by their suit
     *
     * @param cards List of cards to group
     * @return Map of card suits to lists of cards with that suit
     */
    public static Map<CardSuit, List<Card>> groupBySuit(List<Card> cards) {
        return cards.stream()
                .collect(Collectors.groupingBy(Card::suit));
    }

    /**
     * Finds groups of cards by the number of cards in each group
     *
     * @param cards List of cards to analyze
     * @param groupSize Size of the groups to find
     * @return List of card values that have exactly the specified group size,
     *         sorted by numeric value in descending order
     */
    public static List<CardValue> findValueGroups(List<Card> cards, int groupSize) {
        Map<CardValue, List<Card>> valueGroups = groupByValue(cards);

        return valueGroups.entrySet().stream()
                .filter(entry -> entry.getValue().size() == groupSize)
                .map(Map.Entry::getKey)
                .sorted((v1, v2) -> v2.getNumericValue() - v1.getNumericValue())
                .collect(Collectors.toList());
    }

    /**
     * Gets the top N cards by value, excluding cards with specified values
     *
     * @param cards List of cards to search
     * @param excludeValues Values to exclude
     * @param limit Maximum number of cards to return
     * @return List of cards sorted by value descending, limited to the specified count
     */
    public static List<Card> getTopCardsByValue(List<Card> cards, Set<CardValue> excludeValues, int limit) {
        return cards.stream()
                .filter(card -> !excludeValues.contains(card.value()))
                .sorted((c1, c2) -> c2.value().getNumericValue() - c1.value().getNumericValue())
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Gets cards of specific values from a list of cards
     *
     * @param cards List of cards to search
     * @param values Set of values to find
     * @return List of cards that match the specified values
     */
    public static List<Card> getCardsOfValues(List<Card> cards, Set<CardValue> values) {
        return cards.stream()
                .filter(card -> values.contains(card.value()))
                .collect(Collectors.toList());
    }

    /**
     * Gets cards that form a straight
     *
     * @param cards List of cards to check
     * @return List of cards forming a straight or empty list if no straight
     */
    public static List<Card> getStraightCards(List<Card> cards) {
        List<CardValue> distinctValues = cards.stream()
                .map(Card::value)
                .distinct()
                .sorted(Comparator.comparingInt(CardValue::getNumericValue))
                .toList();

        // Check for wheel straight (A-5)
        if (distinctValues.contains(CardValue.TWO) &&
                distinctValues.contains(CardValue.THREE) &&
                distinctValues.contains(CardValue.FOUR) &&
                distinctValues.contains(CardValue.FIVE) &&
                distinctValues.contains(CardValue.ACE)) {

            Set<CardValue> wheelValues = new HashSet<>(Arrays.asList(
                    CardValue.ACE, CardValue.TWO, CardValue.THREE, CardValue.FOUR, CardValue.FIVE));

            return getCardsOfValues(cards, wheelValues).stream()
                    .limit(5)
                    .collect(Collectors.toList());
        }

        // Check for regular straight
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

                return getCardsOfValues(cards, straightValues).stream()
                        .limit(5)
                        .collect(Collectors.toList());
            }
        }

        return Collections.emptyList();
    }

    /**
     * Gets cards sorted by value in descending order
     *
     * @param cards List of cards to sort
     * @return Sorted list of cards
     */
    public static List<Card> getSortedCards(List<Card> cards) {
        return cards.stream()
                .sorted((c1, c2) -> c2.value().getNumericValue() - c1.value().getNumericValue())
                .collect(Collectors.toList());
    }
}