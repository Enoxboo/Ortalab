package main.java.fr.ynov.ortalab.domain.utils;

import main.java.fr.ynov.ortalab.domain.Card;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HandUtils {
    public static List<Card> getSortedCards(List<Card> cards) {
        return cards.stream()
                .sorted(Comparator.comparingInt(card -> card.getValue().getNumericValue()))
                .collect(Collectors.toList());
    }
}
