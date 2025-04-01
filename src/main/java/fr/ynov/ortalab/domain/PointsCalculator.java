package main.java.fr.ynov.ortalab.domain;

import main.java.fr.ynov.ortalab.domain.card.Card;
import main.java.fr.ynov.ortalab.domain.card.CardSuit;
import main.java.fr.ynov.ortalab.domain.card.CardValue;
import main.java.fr.ynov.ortalab.domain.game.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PointsCalculator {
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

    private static int calculateBasePoints(HandEvaluator evaluator) {
        return evaluator.getPoints();
    }

    private static int calculateCardValuePoints(Set<Card> coreCards) {
        return coreCards.stream()
                .mapToInt(card -> card.value().getNumericValue())
                .sum();
    }

    private static int calculateHandTypeBonus(HandType handType, Player player) {
        Map<HandType, Integer> handBonuses = player.getHandTypeDamageBonus();
        return handBonuses.getOrDefault(handType, 0);
    }

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

    private static boolean isHonorCard(CardValue value) {
        // Honor cards are typically Jack, Queen, King, and Ace
        return value == CardValue.JACK || value == CardValue.QUEEN ||
                value == CardValue.KING || value == CardValue.ACE;
    }

    // Enum to categorize card values for bonuses
    public enum CardValueType {
        HONOR,  // J, Q, K, A
        NUMBER, // 2-10
        // Add more as needed
    }

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