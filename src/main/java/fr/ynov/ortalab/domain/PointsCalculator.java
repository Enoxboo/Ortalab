package main.java.fr.ynov.ortalab.domain;

import main.java.fr.ynov.ortalab.domain.card.Card;
import main.java.fr.ynov.ortalab.domain.card.CardSuit;
import main.java.fr.ynov.ortalab.domain.game.Player;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class PointsCalculator {
    /**
     * Calculates the total score for a given selection of cards.
     * Only includes points from cards that are essential to the hand combination.
     *
     * @param selectedCards The cards that the player has selected to play
     * @return The total score for the play
     */
    public static int calculateScore(List<Card> selectedCards, Player player) {
        if (selectedCards == null || selectedCards.isEmpty()) {
            return 0;
        }

        HandEvaluator evaluator = new HandEvaluator(selectedCards);
        String handTypeStr = evaluator.getHandType().toUpperCase().replace(" ", "_");
        HandType handType = HandType.valueOf(handTypeStr);

        int basePoints = evaluator.getPoints();
        Set<Card> coreCards = evaluator.getCoreCards();

        int cardValuePoints = coreCards.stream()
                .mapToInt(card -> card.value().getNumericValue())
                .sum();

        int totalPoints = basePoints + cardValuePoints;

        // Apply hand type bonuses if any
        if (player != null) {
            Map<HandType, Integer> handBonuses = player.getHandTypeDamageBonus();
            if (handBonuses.containsKey(handType)) {
                totalPoints += handBonuses.get(handType);
            }

            // Apply suit bonuses for each card
            Map<CardSuit, Integer> suitBonuses = player.getSuitDamageBonus();
            if (!suitBonuses.isEmpty()) {
                for (Card card : coreCards) {
                    if (suitBonuses.containsKey(card.suit())) {
                        totalPoints += suitBonuses.get(card.suit());
                    }
                }
            }
        }

        return totalPoints;
    }
}