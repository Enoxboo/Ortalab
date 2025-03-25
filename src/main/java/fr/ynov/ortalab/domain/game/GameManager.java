package main.java.fr.ynov.ortalab.domain.game;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.CardSuit;
import main.java.fr.ynov.ortalab.domain.CardValue;
import main.java.fr.ynov.ortalab.domain.PointsCalculator;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GameManager {
    // Game state components
    private Player player;
    private Deck gameDeck;
    private Enemy currentEnemy;
    private GameState gameState;
    private int currentLevel;
    private final int FINAL_BOSS_LEVEL = 5;
    private static final int MAX_HAND_SIZE = 8;
    private static final Random random = new Random();

    // Game configuration
    private static final int INITIAL_PLAYER_HP = 100;

    // Game states
    public enum GameState {
        INITIALIZING,
        DRAWING_CARDS,
        SELECTING_HAND,
        PLAYER_TURN,
        ENEMY_TURN,
        GAME_OVER,
        VICTORY
    }

    /**
     * Constructor to initialize a new game
     */
    public GameManager() {
        initializeGame();
    }

    /**
     * Initialize game components
     */
    private void initializeGame() {
        // Create player with initial health
        player = new Player(INITIAL_PLAYER_HP);

        // Create new deck
        gameDeck = new Deck();

        // Set initial game state
        gameState = GameState.INITIALIZING;
        currentLevel = 1;
    }

    /**
     * Start a new encounter
     */
    public void startEncounter() {
        // Reset deck and shuffle
        gameDeck.reset();

        // Create enemy based on current level
        currentEnemy = createEnemy(currentLevel);

        // Draw initial cards for player
        player.drawCards(gameDeck);

        // Transition to hand selection state
        gameState = GameState.SELECTING_HAND;
    }

    /**
     * Create an enemy based on current level
     *
     * @param level Current game level
     * @return Enemy with scaled difficulty
     */
    private Enemy createEnemy(int level) {
        // Scaling enemy difficulty based on level
        int baseHP = 50 + (level * 10);
        int baseDamage = 10 + (level * 2);
        int cooldown = Math.max(1, 3 - (level / 2));

        // Optional: Add different passive abilities for variety
        Enemy.EnemyPassive passive = level % 2 == 0
                ? new Enemy.PairDamageReductionPassive()
                : null;

        return new Enemy(baseHP, baseDamage, cooldown, passive);
    }

    /**
     * Player selects hand for combat
     *
     * @param selectedCards Cards chosen for active hand
     */
    public void selectHand(List<Card> selectedCards) {
        if (gameState != GameState.SELECTING_HAND) {
            throw new IllegalStateException("Cannot select hand at this time");
        }

        // Select hand and calculate damage
        player.selectHand(selectedCards);
        int playerDamage = player.calculateHandDamage();

        // Apply damage to enemy
        int remainingEnemyHP = currentEnemy.takeDamage(playerDamage, selectedCards);

        // Check if enemy is defeated
        if (remainingEnemyHP <= 0) {
            completeEncounter(true);
            return;
        }

        // Transition to enemy turn
        gameState = GameState.ENEMY_TURN;
        processEnemyTurn();
    }

    /**
     * Process enemy turn
     */
    private void processEnemyTurn() {
        // Reduce enemy cooldown
        currentEnemy.reduceCooldown();

        // Enemy attacks if ready
        if (currentEnemy.canAttack()) {
            currentEnemy.attack(damage -> {
                int remainingPlayerHP = player.takeDamage(damage);

                // Check if player is defeated
                if (remainingPlayerHP <= 0) {
                    gameState = GameState.GAME_OVER;
                }
            });
        }

        // If not game over, return to player turn
        if (gameState != GameState.GAME_OVER) {
            gameState = GameState.SELECTING_HAND;
        }
    }

    /**
     * Complete an encounter (enemy defeated)
     *
     * @param enemyDefeated Whether the enemy was successfully defeated
     */
    private void completeEncounter(boolean enemyDefeated) {
        if (enemyDefeated) {
            // Reward player with gold
            player.addGold(calculateGoldReward());

            // Increment level
            currentLevel++;

            // Check if final boss is reached
            if (currentLevel > FINAL_BOSS_LEVEL) {
                gameState = GameState.VICTORY;
            } else {
                // Prepare for next encounter
                startEncounter();
            }
        }
    }

    /**
     * Calculate gold reward based on current level
     *
     * @return Amount of gold to reward
     */
    private int calculateGoldReward() {
        return 10 + (currentLevel * 5);
    }

    /**
     * Player attempts to discard and draw new cards
     *
     * @param cardsToDiscard Cards to be discarded
     */
    public void playerDiscard(List<Card> cardsToDiscard) {
        if (gameState != GameState.SELECTING_HAND) {
            throw new IllegalStateException("Cannot discard cards at this time");
        }

        // Perform discard
        player.discard(cardsToDiscard, gameDeck);
    }

    // Getters for game state and components
    public GameState getGameState() {
        return gameState;
    }

    public Player getPlayer() {
        return player;
    }

    public Enemy getCurrentEnemy() {
        return currentEnemy;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Main game loop for console-based interaction
     */
    public void runGame() {
        Scanner scanner = new Scanner(System.in);

        // Game initialization
        initializeGame();

        while (gameState != GameState.GAME_OVER &&
                gameState != GameState.VICTORY) {

            // Start new encounter
            startEncounter();

            // Display current hand and prompt for selection
            displayCurrentHand();

            // TODO: Implement proper hand selection logic
            // This is a placeholder for manual selection
            System.out.println("Select your hand (enter card numbers)");
            // Additional game loop logic would go here
        }

        // Display final game result
        if (gameState == GameState.VICTORY) {
            System.out.println("Congratulations! You've defeated the final boss!");
        } else {
            System.out.println("Game Over. Better luck next time!");
        }
    }

    /**
     * Display the player's current hand
     */
    private void displayCurrentHand() {
        List<Card> currentHand = player.getCurrentHand();
        System.out.println("\nYour current hand:");
        for (int i = 0; i < currentHand.size(); i++) {
            System.out.println((i + 1) + ". " + currentHand.get(i).toShortString());
        }
    }

    public static List<Card> createSampleHand() {
        List<Card> hand = new ArrayList<>();
        hand.add(new Card(CardValue.ACE, CardSuit.HEARTS));
        hand.add(new Card(CardValue.TEN, CardSuit.CLUBS));
        hand.add(new Card(CardValue.TEN, CardSuit.DIAMONDS));
        hand.add(new Card(CardValue.TEN, CardSuit.SPADES));
        hand.add(new Card(CardValue.TEN, CardSuit.HEARTS));
        hand.add(new Card(CardValue.NINE, CardSuit.DIAMONDS));
        hand.add(new Card(CardValue.NINE, CardSuit.SPADES));
        hand.add(new Card(CardValue.NINE, CardSuit.HEARTS));
        return hand;
    }

    public static Card createRandomCard() {
        CardValue[] values = CardValue.values();
        CardSuit[] suits = CardSuit.values();

        CardValue value = values[random.nextInt(values.length)];
        CardSuit suit = suits[random.nextInt(suits.length)];

        return new Card(value, suit);
    }

    public static List<Card> generateInitialHand() {
        List<Card> hand = new ArrayList<>();
        while (hand.size() < MAX_HAND_SIZE) {
            hand.add(createRandomCard());
        }
        return hand;
    }
}