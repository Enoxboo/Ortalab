package main.java.fr.ynov.ortalab.domain;

import main.java.fr.ynov.ortalab.domain.checkers.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Collections;

public class HandEvaluator {
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
    private final Set<Card> usedCards;

    public HandEvaluator(List<Card> cards) {
        if (cards == null || cards.size() < 5) {
            throw new IllegalArgumentException("A poker hand must contain at least 5 cards");
        }
        this.cards = new ArrayList<>(cards);
        this.usedCards = new HashSet<>();
        evaluateHand();
    }

    private void evaluateHand() {
        usedCards.clear();

        if (RoyalFlushChecker.isRoyalFlush(cards, usedCards)) {
            handType = "Royal Flush";
            points = ROYAL_FLUSH_POINTS;
        } else if (StraightFlushChecker.isStraightFlush(cards, usedCards)) {
            handType = "Straight Flush";
            points = STRAIGHT_FLUSH_POINTS;
        } else if (FourOfAKindChecker.isFourOfAKind(cards, usedCards)) {
            handType = "Four of a Kind";
            points = FOUR_OF_A_KIND_POINTS;
        } else if (FullHouseChecker.isFullHouse(cards, usedCards)) {
            handType = "Full House";
            points = FULL_HOUSE_POINTS;
        } else if (FlushChecker.isFlush(cards, usedCards)) {
            handType = "Flush";
            points = FLUSH_POINTS;
        } else if (StraightChecker.isStraight(cards, usedCards)) {
            handType = "Straight";
            points = STRAIGHT_POINTS;
        } else if (ThreeOfAKindChecker.isThreeOfAKind(cards, usedCards)) {
            handType = "Three of a Kind";
            points = THREE_OF_A_KIND_POINTS;
        } else if (TwoPairsChecker.isTwoPair(cards, usedCards)) {
            handType = "Two Pair";
            points = TWO_PAIR_POINTS;
        } else if (PairChecker.isPair(cards, usedCards)) {
            handType = "Pair";
            points = PAIR_POINTS;
        } else {
            findHighCard();
            handType = "High Card";
            points = HIGH_CARD_POINTS;
        }
    }

    private void findHighCard() {
        List<Card> sortedCards = cards.stream()
                .sorted((c1, c2) -> c2.getValue().getNumericValue() - c1.getValue().getNumericValue())
                .toList();

        usedCards.addAll(sortedCards.subList(0, Math.min(5, sortedCards.size())));
    }

    private Map<CardValue, Integer> getValueCounts() {
        Map<CardValue, Integer> valueCounts = new HashMap<>();
        for (Card card : cards) {
            valueCounts.put(card.getValue(), valueCounts.getOrDefault(card.getValue(), 0) + 1);
        }
        return valueCounts;
    }


    public String getHandType() {
        return handType;
    }

    public int getPoints() {
        return points;
    }

    public Set<Card> getUsedCards() {
        return Collections.unmodifiableSet(usedCards);
    }

    public int calculateTotalPoints() {
        int totalPoints = points;

        for (Card card : usedCards) {
            totalPoints += card.getValue().getNumericValue();
        }

        return totalPoints;
    }

    @Override
    public String toString() {
        return handType + " (" + points + " base points, " + calculateTotalPoints() + " total points)";
    }
}