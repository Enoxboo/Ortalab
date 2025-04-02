package main.java.fr.ynov.ortalab.domain.game;

import main.java.fr.ynov.ortalab.domain.card.Card;
import main.java.fr.ynov.ortalab.domain.card.CardSuit;
import main.java.fr.ynov.ortalab.domain.card.CardValue;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Calculates score points for poker hands based on various factors including
 * the hand type, card values, and player-specific bonuses.
 */
public class PointsCalculator {

    /**
     * Calculates the total score for a set of selected cards.
     *
     * @param selectedCards The cards to evaluate
     * @param player The player who owns these cards (for bonus calculations)
     * @return The total calculated score
     */
    public static int calculateScore(List<Card> selectedCards, Player player) {
        if (selectedCards == null || selectedCards.isEmpty()) {
            return 0;
        }

        HandEvaluator evaluator = new HandEvaluator(selectedCards);
        String handTypeStr = evaluator.getHandType().toUpperCase().replace(" ", "_");
        HandType handType = HandType.valueOf(handTypeStr);
        Set<Card> coreCards = evaluator.getCoreCards();

        int totalPoints = calculateBasePoints(evaluator);
        totalPoints += calculateCardValuePoints(coreCards);

        if (player != null) {
            totalPoints += calculateHandTypeBonus(handType, player);
            totalPoints += calculateSuitBonus(coreCards, player);
            totalPoints += calculateHonorCardBonus(coreCards, player);
            totalPoints += calculateCardCountBonus(selectedCards, player);
            totalPoints += calculateRejectionBonus(selectedCards, coreCards, player);
        }

        return totalPoints;
    }

    /**
     * Enum to categorize card values for bonus calculations.
     */
    public enum CardValueType {
        HONOR,  // J, Q, K, A
        NUMBER  // 2-10
        // Add more as needed
    }

    /**
     * Calculates the base points from the hand's type.
     */
    private static int calculateBasePoints(HandEvaluator evaluator) {
        return evaluator.getPoints();
    }

    /**
     * Calculates points based on the numeric values of the core cards.
     */
    private static int calculateCardValuePoints(Set<Card> coreCards) {
        return coreCards.stream()
                .mapToInt(card -> card.value().getNumericValue())
                .sum();
    }

    /**
     * Applies player-specific bonuses based on the hand type.
     */
    private static int calculateHandTypeBonus(HandType handType, Player player) {
        Map<HandType, Integer> handBonuses = player.getHandTypeDamageBonus();
        return handBonuses.getOrDefault(handType, 0);
    }

    /**
     * Applies player-specific bonuses based on card suits.
     */
    private static int calculateSuitBonus(Set<Card> coreCards, Player player) {
        Map<CardSuit, Integer> suitBonuses = player.getSuitDamageBonus();
        if (suitBonuses.isEmpty()) {
            return 0;
        }

        int suitBonus = 0;
        for (Card card : coreCards) {
            suitBonus += suitBonuses.getOrDefault(card.suit(), 0);
        }

        return suitBonus;
    }

    /**
     * Applies player-specific bonuses for honor cards (J, Q, K, A).
     */
    private static int calculateHonorCardBonus(Set<Card> coreCards, Player player) {
        Map<CardValueType, Integer> honorCardBonuses = player.getCardValueTypeBonus();
        if (honorCardBonuses.isEmpty()) {
            return 0;
        }

        int honorBonus = 0;
        for (Card card : coreCards) {
            CardValue value = card.value();
            if (isHonorCard(value)) {
                honorBonus += honorCardBonuses.getOrDefault(CardValueType.HONOR, 0);
            }
            // You can add more card value type checks here
        }

        return honorBonus;
    }

    /**
     * Determines if a card value is considered an honor card.
     */
    private static boolean isHonorCard(CardValue value) {
        // Honor cards are typically Jack, Queen, King, and Ace
        return value == CardValue.JACK || value == CardValue.QUEEN ||
                value == CardValue.KING || value == CardValue.ACE;
    }

    /**
     * Applies player-specific bonuses based on the total number of cards in the hand.
     */
    private static int calculateCardCountBonus(List<Card> selectedCards, Player player) {
        Map<Integer, Integer> cardCountBonuses = player.getCardCountBonus();
        if (cardCountBonuses.isEmpty()) {
            return 0;
        }

        int bonus = 0;
        int cardCount = selectedCards.size();

        // Apply bonus for each minimum card count threshold met
        for (Map.Entry<Integer, Integer> entry : cardCountBonuses.entrySet()) {
            int minCount = entry.getKey();
            if (cardCount >= minCount) {
                bonus += entry.getValue();
            }
        }

        return bonus;
    }

    /**
     * Calculates bonus points for cards that contributed to the hand but were not part
     * of the core combination.
     */
    private static int calculateRejectionBonus(List<Card> selectedCards, Set<Card> coreCards, Player player) {
        int rejectionBonusPerCard = player.getRejectionBonus();
        if (rejectionBonusPerCard <= 0) {
            return 0;
        }

        // Create a set of the selected cards to compare with core cards
        Set<Card> selectedCardSet = new HashSet<>(selectedCards);

        // Find cards that are in the attack but not in the core cards
        Set<Card> rejectedCards = new HashSet<>(selectedCardSet);
        rejectedCards.removeAll(coreCards);

        // Calculate bonus based on number of rejected cards
        return rejectedCards.size() * rejectionBonusPerCard;
    }
}