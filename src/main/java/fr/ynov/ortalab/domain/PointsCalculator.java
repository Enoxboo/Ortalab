package main.java.fr.ynov.ortalab.domain;

import java.util.List;
import java.util.Set;

public class PointsCalculator {
    /**
     * Calculates the total score for a given selection of cards.
     * Only includes points from cards that are essential to the hand combination.
     *
     * @param selectedCards The cards that the player has selected to play
     * @return The total score for the play
     */
    public static int calculateScore(List<Card> selectedCards) {
        if (selectedCards == null || selectedCards.isEmpty()) {
            return 0;
        }

        // Use the HandEvaluator to find the best hand and used cards
        HandEvaluator evaluator = new HandEvaluator(selectedCards);

        // Get base points from the hand type
        int basePoints = evaluator.getPoints();

        // Get only the core cards that form the hand combination (not kickers)
        Set<Card> coreCards = evaluator.getCoreCards();

        // Sum the numeric values of the core cards
        int cardValuePoints = coreCards.stream()
                .mapToInt(card -> card.getValue().getNumericValue())
                .sum();

        // Return the total score
        return basePoints + cardValuePoints;
    }

    /**
     * Returns a string representation of the score calculation.
     *
     * @param selectedCards The cards that the player has selected to play
     * @return A string explaining the score calculation
     */
    public static String getScoreBreakdown(List<Card> selectedCards) {
        if (selectedCards == null || selectedCards.isEmpty()) {
            return "No cards selected, score is 0.";
        }

        HandEvaluator evaluator = new HandEvaluator(selectedCards);
        String handType = evaluator.getHandType();
        int basePoints = evaluator.getPoints();

        // Get only the core cards for the score calculation
        Set<Card> coreCards = evaluator.getCoreCards();

        int cardValuePoints = coreCards.stream()
                .mapToInt(card -> card.getValue().getNumericValue())
                .sum();

        int totalPoints = basePoints + cardValuePoints;

        StringBuilder builder = new StringBuilder();
        builder.append("Hand: ").append(handType).append("\n");
        builder.append("Base points: ").append(basePoints).append("\n");
        builder.append("Used cards: ");

        for (Card card : coreCards) {
            builder.append(card.toShortString()).append(" ");
        }

        builder.append("\n");
        builder.append("Card value points: ").append(cardValuePoints).append("\n");
        builder.append("Total score: ").append(totalPoints);

        return builder.toString();
    }
}