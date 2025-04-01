package main.java.fr.ynov.ortalab.domain.checkers;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.HandType;

import java.util.List;
import java.util.Set;

/**
 * Interface for poker hand checkers.
 */
public interface HandChecker {
    /**
     * Checks if the given cards form a specific hand type.
     *
     * @param cards The cards to check
     * @param usedCards A set to be populated with the cards used in the hand
     * @param coreCards A set to be populated with the core cards of the hand (no kickers)
     * @return true if the hand type is found, false otherwise
     */
    boolean checkHand(List<Card> cards, Set<Card> usedCards, Set<Card> coreCards);

    /**
     * Gets the hand type this checker is responsible for.
     *
     * @return The hand type
     */
    HandType getHandType();
}