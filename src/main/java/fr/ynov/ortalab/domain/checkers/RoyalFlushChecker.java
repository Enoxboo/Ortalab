package main.java.fr.ynov.ortalab.domain.checkers;

import main.java.fr.ynov.ortalab.domain.card.Card;
import main.java.fr.ynov.ortalab.domain.card.CardSuit;
import main.java.fr.ynov.ortalab.domain.card.CardValue;
import main.java.fr.ynov.ortalab.domain.HandType;
import main.java.fr.ynov.ortalab.domain.utils.HandUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;

/**
 * Checks for a Royal Flush hand (Ten through Ace of the same suit).
 * This is the highest possible hand in poker.
 */
public class RoyalFlushChecker implements HandChecker {

    /**
     * The specific card values required for a Royal Flush (10-A).
     */
    private static final Set<CardValue> ROYAL_VALUES = new HashSet<>(Arrays.asList(
            CardValue.TEN, CardValue.JACK, CardValue.QUEEN, CardValue.KING, CardValue.ACE
    ));

    @Override
    public boolean checkHand(List<Card> cards, Set<Card> usedCards, Set<Card> coreCards) {
        // Group cards by suit
        Map<CardSuit, List<Card>> suitGroups = HandUtils.groupBySuit(cards);

        // Check each suit group for a royal flush
        for (Map.Entry<CardSuit, List<Card>> entry : suitGroups.entrySet()) {
            if (entry.getValue().size() < 5) {
                continue;
            }

            List<Card> sameSuitCards = entry.getValue();

            // Check if all royal values exist in this suit
            boolean hasAllRoyalValues = ROYAL_VALUES.stream()
                    .allMatch(value -> sameSuitCards.stream()
                            .anyMatch(card -> card.value() == value));

            if (hasAllRoyalValues) {
                List<Card> royalCards = sameSuitCards.stream()
                        .filter(card -> ROYAL_VALUES.contains(card.value()))
                        .limit(5)
                        .toList();

                usedCards.addAll(royalCards);
                coreCards.addAll(royalCards);
                return true;
            }
        }

        return false;
    }

    @Override
    public HandType getHandType() {
        return HandType.ROYAL_FLUSH;
    }
}