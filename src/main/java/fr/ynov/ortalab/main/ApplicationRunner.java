package main.java.fr.ynov.ortalab.main;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.CardSuit;
import main.java.fr.ynov.ortalab.domain.CardValue;
import main.java.fr.ynov.ortalab.domain.PointsCalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Example class to demonstrate the poker hand evaluation and scoring
 * with the added discard mechanic.
 */
public class ApplicationRunner {
    public static void main(String[] args) {
        // Create a sample hand
        List<Card> playerHand = createSampleHand();
        playRound(playerHand);
    }

    /**
     * Plays one round of the game with the given hand.
     *
     * @param playerHand The player's hand
     */
    private static void playRound(List<Card> playerHand) {
        Scanner scanner = new Scanner(System.in);

        // Display the player's hand
        displayHand(playerHand, "Your hand:");

        // Let the player select cards
        System.out.println("\nSelect cards to play or discard (enter card numbers separated by spaces):");
        List<Card> selectedCards = selectCardsInteractive(playerHand);

        if (selectedCards.isEmpty()) {
            System.out.println("No cards selected. Please select at least one card.");
            return;
        }

        // Ask the player what they want to do with the selected cards
        System.out.println("\nYou've selected these cards:");
        displaySelectedCards(selectedCards);

        System.out.println("\nWhat would you like to do?");
        System.out.println("1. Play these cards");
        System.out.println("2. Discard these cards and draw new ones");

        int choice = promptForChoice(scanner, 1, 2);

        if (choice == 1) {
            // Play the selected cards
            playSelectedCards(selectedCards);
        } else {
            // Discard the selected cards and draw new ones
            discardAndDraw(playerHand, selectedCards);
        }
    }

    /**
     * Plays the selected cards and shows the score.
     *
     * @param selectedCards The cards to play
     */
    private static void playSelectedCards(List<Card> selectedCards) {
        System.out.println("\nPlaying selected cards...");
        System.out.println("\nScore breakdown:");
        System.out.println(PointsCalculator.getScoreBreakdown(selectedCards));
    }

    /**
     * Discards the selected cards and draws replacements.
     *
     * @param playerHand The player's hand
     * @param selectedCards The cards to discard
     */
    private static void discardAndDraw(List<Card> playerHand, List<Card> selectedCards) {
        System.out.println("\nDiscarding selected cards and drawing new ones...");

        // Remove selected cards from player's hand
        playerHand.removeAll(selectedCards);

        // Draw new cards (in a real game, this would draw from a deck)
        for (int i = 0; i < selectedCards.size(); i++) {
            playerHand.add(createRandomCard());
        }

        // Show the new hand
        displayHand(playerHand, "\nYour new hand:");

        // Continue with the new hand
        playRound(playerHand);
    }

    /**
     * Creates a sample hand of 5 cards.
     *
     * @return A list of 5 cards
     */
    private static List<Card> createSampleHand() {
        List<Card> hand = new ArrayList<>();
        hand.add(new Card(CardValue.ACE, CardSuit.HEARTS));
        hand.add(new Card(CardValue.TEN, CardSuit.CLUBS));
        hand.add(new Card(CardValue.TEN, CardSuit.DIAMONDS));
        hand.add(new Card(CardValue.TEN, CardSuit.SPADES));
        hand.add(new Card(CardValue.TEN, CardSuit.HEARTS));
        return hand;
    }

    /**
     * Creates a random card for demonstration purposes.
     * In a real game, this would draw from a deck.
     *
     * @return A random card
     */
    private static Card createRandomCard() {
        CardValue[] values = CardValue.values();
        CardSuit[] suits = CardSuit.values();

        CardValue value = values[(int)(Math.random() * values.length)];
        CardSuit suit = suits[(int)(Math.random() * suits.length)];

        return new Card(value, suit);
    }

    /**
     * Displays the player's hand.
     *
     * @param hand The hand to display
     * @param title The title to show above the hand
     */
    private static void displayHand(List<Card> hand, String title) {
        System.out.println(title);
        for (int i = 0; i < hand.size(); i++) {
            System.out.println((i + 1) + ". " + hand.get(i).toShortString() + " (" + hand.get(i) + ")");
        }
    }

    /**
     * Displays only the selected cards.
     *
     * @param selectedCards The selected cards to display
     */
    private static void displaySelectedCards(List<Card> selectedCards) {
        for (Card card : selectedCards) {
            System.out.println(card.toShortString() + " (" + card + ")");
        }
    }

    /**
     * Prompts the user for a choice between min and max (inclusive).
     *
     * @param scanner The scanner to use for input
     * @param min The minimum valid choice
     * @param max The maximum valid choice
     * @return The user's choice
     */
    private static int promptForChoice(Scanner scanner, int min, int max) {
        int choice = -1;
        while (choice < min || choice > max) {
            System.out.print("Enter your choice (" + min + "-" + max + "): ");
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice < min || choice > max) {
                    System.out.println("Invalid choice. Please enter a number between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        return choice;
    }

    /**
     * Lets the player select cards using console input.
     *
     * @param playerHand The player's hand
     * @return The selected cards
     */
    private static List<Card> selectCardsInteractive(List<Card> playerHand) {
        Scanner scanner = new Scanner(System.in);
        List<Card> selectedCards = new ArrayList<>();

        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return selectedCards;
        }

        String[] selections = input.split("\\s+");
        for (String selection : selections) {
            try {
                int index = Integer.parseInt(selection) - 1;
                if (index >= 0 && index < playerHand.size() &&
                        !selectedCards.contains(playerHand.get(index))) {
                    selectedCards.add(playerHand.get(index));
                }
            } catch (NumberFormatException e) {
                // Ignore invalid input
            }
        }

        return selectedCards;
    }
}