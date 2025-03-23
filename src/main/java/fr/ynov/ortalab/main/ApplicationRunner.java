package main.java.fr.ynov.ortalab.main;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.CardSuit;
import main.java.fr.ynov.ortalab.domain.CardValue;
import main.java.fr.ynov.ortalab.domain.PointsCalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Example class to demonstrate the poker hand evaluation and scoring.
 */
public class ApplicationRunner {
    public static void main(String[] args) {
        // Create a sample hand
        List<Card> playerHand = new ArrayList<>();
        playerHand.add(new Card(CardValue.ACE, CardSuit.HEARTS));
        playerHand.add(new Card(CardValue.TEN, CardSuit.CLUBS));
        playerHand.add(new Card(CardValue.TEN, CardSuit.DIAMONDS));
        playerHand.add(new Card(CardValue.TEN, CardSuit.SPADES));
        playerHand.add(new Card(CardValue.TEN, CardSuit.HEARTS));

        // Display the player's hand
        System.out.println("Your hand:");
        for (int i = 0; i < playerHand.size(); i++) {
            System.out.println((i + 1) + ". " + playerHand.get(i).toShortString());
        }

        // Let the player select cards
        List<Card> selectedCards = selectCards(playerHand);

        // Calculate and display the score
        System.out.println("\nScore breakdown:");
        System.out.println(PointsCalculator.getScoreBreakdown(selectedCards));
    }

    /**
     * Simulates the player selecting cards to play.
     * In a real game, this would be based on user input.
     *
     * @param playerHand The player's hand
     * @return The selected cards
     */
    private static List<Card> selectCards(List<Card> playerHand) {
        // This is a placeholder for user interaction
        // In this example, we'll just select all cards
        System.out.println("\nSelecting all cards for this example...");
        return new ArrayList<>(playerHand);
    }

    /**
     * Interactive method to let the player select cards using console input.
     *
     * @param playerHand The player's hand
     * @return The selected cards
     */
    private static List<Card> selectCardsInteractive(List<Card> playerHand) {
        Scanner scanner = new Scanner(System.in);
        List<Card> selectedCards = new ArrayList<>();

        System.out.println("\nSelect cards to play (enter card numbers separated by spaces):");
        String input = scanner.nextLine();

        String[] selections = input.split("\\s+");
        for (String selection : selections) {
            try {
                int index = Integer.parseInt(selection) - 1;
                if (index >= 0 && index < playerHand.size()) {
                    selectedCards.add(playerHand.get(index));
                }
            } catch (NumberFormatException e) {
                // Ignore invalid input
            }
        }

        return selectedCards;
    }
}