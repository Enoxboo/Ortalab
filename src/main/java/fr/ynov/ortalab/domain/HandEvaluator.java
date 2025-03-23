package main.java.fr.ynov.ortalab.domain;

import main.java.fr.ynov.ortalab.domain.checkers.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;

public class HandEvaluator {
    // Points values for each hand type
    private static final int HIGH_CARD_POINTS = 10;
    private static final int PAIR_POINTS = 20;
    private static final int TWO_PAIR_POINTS = 40;
    private static final int THREE_OF_A_KIND_POINTS = 80;
    private static final int STRAIGHT_POINTS = 100;
    private static final int FLUSH_POINTS = 125;
    private static final int FULL_HOUSE_POINTS = 175;
    private static final int FOUR_OF_A_KIND_POINTS = 400;
    private static final int STRAIGHT_FLUSH_POINTS = 600;
    private static final int ROYAL_FLUSH_POINTS = 2000;

    private final List<Card> cards;
    private String handType;
    private int points;
    private final Set<Card> usedCards;  // All cards used in the hand (including kickers)
    private final Set<Card> coreCards;  // Only the essential cards for the combination

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
        evaluateHand();
    }

    /**
     * Evaluates the hand to find the best possible poker combination.
     */
    private void evaluateHand() {
        usedCards.clear();
        coreCards.clear();

        // Check for each hand type in order from highest to lowest
        if (cards.size() >= 5 && RoyalFlushChecker.isRoyalFlush(cards, usedCards)) {
            handType = "Royal Flush";
            points = ROYAL_FLUSH_POINTS;
            coreCards.addAll(usedCards); // In a royal flush, all cards are core
        } else if (cards.size() >= 5 && StraightFlushChecker.isStraightFlush(cards, usedCards)) {
            handType = "Straight Flush";
            points = STRAIGHT_FLUSH_POINTS;
            coreCards.addAll(usedCards); // In a straight flush, all cards are core
        } else if (cards.size() >= 5 && FourOfAKindChecker.isFourOfAKind(cards, usedCards)) {
            handType = "Four of a Kind";
            points = FOUR_OF_A_KIND_POINTS;
            // Only the four matching cards are core, not the kicker
            FourOfAKindChecker.identifyFourOfAKindCoreCards(usedCards, coreCards);
        } else if (cards.size() >= 5 && FullHouseChecker.isFullHouse(cards, usedCards)) {
            handType = "Full House";
            points = FULL_HOUSE_POINTS;
            // Both the three of a kind and the pair are core
            coreCards.addAll(usedCards);
        } else if (cards.size() >= 5 && FlushChecker.isFlush(cards, usedCards)) {
            handType = "Flush";
            points = FLUSH_POINTS;
            coreCards.addAll(usedCards); // In a flush, all cards are core
        } else if (cards.size() >= 5 && StraightChecker.isStraight(cards, usedCards)) {
            handType = "Straight";
            points = STRAIGHT_POINTS;
            coreCards.addAll(usedCards); // In a straight, all cards are core
        } else if (cards.size() >= 3 && ThreeOfAKindChecker.isThreeOfAKind(cards, usedCards)) {
            handType = "Three of a Kind";
            points = THREE_OF_A_KIND_POINTS;
            // Only the three matching cards are core, not the kickers
            ThreeOfAKindChecker.identifyThreeOfAKindCoreCards(usedCards, coreCards);
        } else if (cards.size() >= 4 && TwoPairsChecker.isTwoPair(cards, usedCards)) {
            handType = "Two Pair";
            points = TWO_PAIR_POINTS;
            // Only the two pairs are core, not the kicker
            TwoPairsChecker.identifyTwoPairCoreCards(usedCards, coreCards);
        } else if (cards.size() >= 2 && PairChecker.isPair(cards, usedCards)) {
            handType = "Pair";
            points = PAIR_POINTS;
            // Only the pair cards are core, not the kickers
            PairChecker.identifyPairCoreCards(usedCards, coreCards);
        } else {
            HighCardChecker.isHighCard(cards, usedCards);
            handType = "High Card";
            points = HIGH_CARD_POINTS;
            // The single high card is core
            coreCards.addAll(usedCards);
        }
    }

    /**
     * Gets the type of hand (e.g., "Pair", "Flush").
     *
     * @return The hand type
     */
    public String getHandType() {
        return handType;
    }

    /**
     * Gets the base points for this hand type.
     *
     * @return The base points
     */
    public int getPoints() {
        return points;
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
        return handType + " (" + points + " base points";
    }
}