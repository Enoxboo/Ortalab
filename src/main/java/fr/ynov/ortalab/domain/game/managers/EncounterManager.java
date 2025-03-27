package main.java.fr.ynov.ortalab.domain.game.managers;

import main.java.fr.ynov.ortalab.domain.game.Player;
import main.java.fr.ynov.ortalab.domain.game.Deck;
import main.java.fr.ynov.ortalab.domain.game.Enemy;

import java.util.Random;

public class EncounterManager {
    private Player player;
    private Deck gameDeck;
    private Enemy currentEnemy;
    private int currentLevel;
    private static final int FINAL_BOSS_LEVEL = 5;
    private static final Random random = new Random();
    private EnemyPool enemyPool;

    public EncounterManager(Player player, Deck gameDeck) {
        this.player = player;
        this.gameDeck = gameDeck;
        this.currentLevel = 1;
        this.enemyPool = new EnemyPool();
    }

    public void startFirstEncounter() {
        startEncounter();
    }

    public void startEncounter() {
        // Reset deck and shuffle
        gameDeck.reset();

        // Create enemy based on current level
        currentEnemy = createEnemy(currentLevel);

        // Draw initial cards for player
        player.drawCards(gameDeck);
    }

    public Enemy createEnemy(int level) {
        return enemyPool.getRandomEnemyForLevel(level);
    }

    public void completeEncounter(boolean enemyDefeated) {
        if (enemyDefeated) {
            // Reward player with gold
            player.addGold(calculateGoldReward());

            // Increment level
            currentLevel++;
        }
    }

    public boolean isGameComplete() {
        return currentLevel > FINAL_BOSS_LEVEL;
    }

    private int calculateGoldReward() {
        return 10 + (currentLevel * 5);
    }

    // Getters
    public Enemy getCurrentEnemy() {
        return currentEnemy;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }
}