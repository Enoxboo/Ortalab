package main.java.fr.ynov.ortalab.domain.game.managers;

import main.java.fr.ynov.ortalab.config.GameConfig;
import main.java.fr.ynov.ortalab.domain.exceptions.DeckException;
import main.java.fr.ynov.ortalab.domain.game.Player;
import main.java.fr.ynov.ortalab.domain.game.Deck;
import main.java.fr.ynov.ortalab.domain.game.Enemy;
import main.java.fr.ynov.ortalab.factories.EnemyFactory;

/**
 * Manages enemy encounters throughout the game.
 * Handles enemy creation, encounter progression, and rewards.
 */
public class EncounterManager {
    private final Player player;
    private final Deck gameDeck;
    private Enemy currentEnemy;
    private int currentLevel;
    private static final int FINAL_BOSS_LEVEL = GameConfig.FINAL_BOSS_LEVEL;
    private static final int[] SHOP_LEVELS = GameConfig.SHOP_LEVELS;
    private final EnemyFactory enemyFactory;

    /**
     * Creates a new encounter manager with the given player and deck.
     * Initializes the game at level 1.
     */
    public EncounterManager(Player player, Deck gameDeck) {
        this.player = player;
        this.gameDeck = gameDeck;
        this.currentLevel = 1;
        this.enemyFactory = new EnemyFactory();
    }

    // --- Core encounter lifecycle methods ---

    /**
     * Starts the first encounter of the game.
     * @throws DeckException if there's an issue with the deck operations
     */
    public void startFirstEncounter() throws DeckException {
        startEncounter();
    }

    /**
     * Prepares a new encounter by resetting the deck, creating an enemy,
     * and drawing cards for the player.
     * @throws DeckException if there's an issue with the deck operations
     */
    public void startEncounter() throws DeckException {
        gameDeck.reset();
        currentEnemy = createEnemy(currentLevel);
        player.drawCards(gameDeck);
    }

    /**
     * Handles the completion of an encounter.
     * Awards gold and advances to the next level if the enemy was defeated.
     * @param enemyDefeated true if the player won the encounter
     */
    public void completeEncounter(boolean enemyDefeated) {
        if (enemyDefeated) {
            player.addGold(calculateGoldReward());
            player.applyPostBattleEffects();
            currentLevel++;
        }
    }

    // --- Enemy creation ---

    /**
     * Creates an appropriate enemy for the current level.
     * @param level the current game level
     * @return a new enemy appropriate for the level
     */
    public Enemy createEnemy(int level) {
        return enemyFactory.createEnemyForLevel(level);
    }

    // --- Game progression state checks ---

    /**
     * Checks if the player should visit a shop after the current level.
     * @return true if the current level is a shop level
     */
    public boolean shouldVisitShop() {
        for (int shopLevel : SHOP_LEVELS) {
            if (currentLevel == shopLevel) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the game is complete (final boss defeated).
     * @return true if the player has completed all levels
     */
    public boolean isGameComplete() {
        return currentLevel > FINAL_BOSS_LEVEL;
    }

    // --- Helper methods ---

    /**
     * Calculates gold reward based on current level.
     * @return the amount of gold to award
     */
    private int calculateGoldReward() {
        return GameConfig.BASE_GOLD_REWARD + (currentLevel * GameConfig.GOLD_REWARD_MULTIPLIER);
    }

    // --- Getters ---

    public Enemy getCurrentEnemy() {
        return currentEnemy;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }
}