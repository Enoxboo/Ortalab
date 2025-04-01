package main.java.fr.ynov.ortalab.domain;

import main.java.fr.ynov.ortalab.domain.card.Card;
import main.java.fr.ynov.ortalab.domain.checkers.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HandEvaluator {
    private final List<Card> cards;
    private HandType handType;
    private final Set<Card> usedCards;  // All cards used in the hand (including kickers)
    private final Set<Card> coreCards;  // Only the essential cards for the combination
    private final List<HandChecker> checkers;

    /**
     * Creates a new HandEvaluator for the given cards.
     *
     * @param cards The cards to evaluate
     */
    public HandEvaluator(List<Card> cards) {
        if (cards == null || cards.isEmpty()) {
            throw new IllegalArgumentException("A hand must contain at least one card");
        }
        this.cards = new ArrayList<>(cards);
        this.usedCards = new HashSet<>();
        this.coreCards = new HashSet<>();

        // Initialize checkers in order from highest to lowest rank
        this.checkers = new ArrayList<>();
        initializeCheckers();

        evaluateHand();
    }

    /**
     * Initializes all the hand checkers in order from highest to lowest rank.
     */
    private void initializeCheckers() {
        checkers.add(new RoyalFlushChecker());
        checkers.add(new StraightFlushChecker());
        checkers.add(new FourOfAKindChecker());
        checkers.add(new FullHouseChecker());
        checkers.add(new FlushChecker());
        checkers.add(new StraightChecker());
        checkers.add(new ThreeOfAKindChecker());
        checkers.add(new TwoPairsChecker());
        checkers.add(new PairChecker());
        checkers.add(new HighCardChecker());
    }

    /**
     * Evaluates the hand to find the best possible poker combination.
     */
    private void evaluateHand() {
        usedCards.clear();
        coreCards.clear();

        // Try each checker in order (from the highest rank to lowest)
        for (HandChecker checker : checkers) {
            if (checker.checkHand(cards, usedCards, coreCards)) {
                handType = checker.getHandType();
                return;
            }
        }

        // If we get here, we have nothing (should never happen with HighCardChecker)
        handType = HandType.HIGH_CARD;
    }

    /**
     * Gets the type of hand (e.g., "Pair", "Flush").
     *
     * @return The hand type
     */
    public String getHandType() {
        return handType.getName();
    }

    /**
     * Gets the base points for this hand type.
     *
     * @return The base points
     */
    public int getPoints() {
        return handType.getBasePoints();
    }

    /**
     * Gets all cards that were used in the hand, including kickers.
     *
     * @return All used cards
     */
    public Set<Card> getUsedCards() {
        return Collections.unmodifiableSet(usedCards);
    }

    /**
     * Gets only the core cards that form the hand combination, not including kickers.
     *
     * @return The core cards
     */
    public Set<Card> getCoreCards() {
        return Collections.unmodifiableSet(coreCards);
    }

    @Override
    public String toString() {
        return handType + " (" + handType.getBasePoints() + " base points)";
    }
}