package main.java.fr.ynov.ortalab.main;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.game.GameManager;
import main.java.fr.ynov.ortalab.domain.game.Player;
import main.java.fr.ynov.ortalab.domain.game.Enemy;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ApplicationRunner {
    private static GameManager gameManager;
    private static Scanner scanner;

    public static void main(String[] args) {
        gameManager = new GameManager();
        scanner = new Scanner(System.in);

        // Start the first encounter
        gameManager.startEncounter();

        // Main game loop
        while (true) {
            // Display game state
            displayGameStatus();

            // Player's turn to select cards
            List<Card> selectedCards = selectCards();

            // Prompt for action
            System.out.println("\nChoose an action:");
            System.out.println("1. Play selected cards");
            System.out.println("2. Discard selected cards");

            int choice = promptForChoice(1, 2);

            if (choice == 1) {
                // Play the selected cards
                playSelectedCards(selectedCards);
            } else {
                // Discard the selected cards
                discardSelectedCards(selectedCards);
            }

            // Check for game-ending conditions
            if (isGameOver()) {
                break;
            }
        }

        // Close scanner
        scanner.close();
    }

    /**
     * Display current game status
     */
    private static void displayGameStatus() {
        Player player = gameManager.getPlayer();
        Enemy enemy = gameManager.getCurrentEnemy();

        // Player information
        System.out.println("\n--- Player Status ---");
        System.out.println("HP: " + player.getHealthPoints() + "/" + player.getMaxHealthPoints());
        System.out.println("Discards left: " + player.getRemainingDiscards());

        // Enemy information
        System.out.println("\n--- Enemy Status ---");
        System.out.println("HP: " + enemy.getHealthPoints());
        System.out.println("Attack Damage: " + enemy.getAttackDamage());
        System.out.println("Attack Cooldown: " + enemy.getCurrentCooldown());

        // Display current hand
        System.out.println("\n--- Your Hand ---");
        List<Card> currentHand = player.getCurrentHand();
        for (int i = 0; i < currentHand.size(); i++) {
            System.out.println((i + 1) + ". " + currentHand.get(i).toShortString());
        }
    }

    /**
     * Select cards from the current hand
     */
    private static List<Card> selectCards() {
        Player player = gameManager.getPlayer();
        List<Card> currentHand = player.getCurrentHand();
        List<Card> selectedCards = new ArrayList<>();

        while (true) {
            System.out.println("\nSelect cards (1-5) to play or discard. Enter 0 to finish selection.");
            int cardIndex = promptForChoice(0, currentHand.size());

            if (cardIndex == 0) {
                if (selectedCards.isEmpty()) {
                    System.out.println("You must select at least one card.");
                    continue;
                }
                break;
            }

            Card selectedCard = currentHand.get(cardIndex - 1);
            if (selectedCards.contains(selectedCard)) {
                System.out.println("You've already selected this card.");
                continue;
            }

            selectedCards.add(selectedCard);

            if (selectedCards.size() == 5) {
                break;
            }
        }

        return selectedCards;
    }

    /**
     * Play selected cards against the enemy
     */
    private static void playSelectedCards(List<Card> selectedCards) {
        try {
            // Select the hand and process combat
            gameManager.selectHand(selectedCards);
            System.out.println("Cards played successfully!");
        } catch (Exception e) {
            System.out.println("Error playing cards: " + e.getMessage());
        }
    }

    /**
     * Discard selected cards
     */
    private static void discardSelectedCards(List<Card> selectedCards) {
        try {
            // Perform discard
            gameManager.playerDiscard(selectedCards);
            System.out.println("Cards discarded successfully!");
        } catch (Exception e) {
            System.out.println("Error discarding cards: " + e.getMessage());
        }
    }

    /**
     * Check if the game is over
     */
    private static boolean isGameOver() {
        GameManager.GameState gameState = gameManager.getGameState();

        if (gameState == GameManager.GameState.GAME_OVER) {
            System.out.println("Game Over! You were defeated.");
            return true;
        }

        if (gameState == GameManager.GameState.VICTORY) {
            System.out.println("Congratulations! You've won the game!");
            return true;
        }

        return false;
    }

    /**
     * Prompt for a choice between min and max (inclusive)
     */
    private static int promptForChoice(int min, int max) {
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
}