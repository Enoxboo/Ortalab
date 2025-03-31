package main.java.fr.ynov.ortalab.domain.game.managers;

import main.java.fr.ynov.ortalab.config.GameConfig;
import main.java.fr.ynov.ortalab.domain.exceptions.DeckException;
import main.java.fr.ynov.ortalab.domain.game.Player;
import main.java.fr.ynov.ortalab.domain.game.Deck;
import main.java.fr.ynov.ortalab.domain.game.Enemy;

import java.util.Random;

public class EncounterManager {
    private final Player player;
    private final Deck gameDeck;
    private Enemy currentEnemy;
    private int currentLevel;
    private static final int FINAL_BOSS_LEVEL = GameConfig.FINAL_BOSS_LEVEL;
    private static final Random random = new Random();
    private final EnemyPool enemyPool;

    // Add shop levels as constants
    private static final int[] SHOP_LEVELS = {1, 3};

    public EncounterManager(Player player, Deck gameDeck) {
        this.player = player;
        this.gameDeck = gameDeck;
        this.currentLevel = 1;
        this.enemyPool = new EnemyPool();
    }

    public void startFirstEncounter() throws DeckException {
        startEncounter();
    }

    public void startEncounter() throws DeckException {
        gameDeck.reset();

        currentEnemy = createEnemy(currentLevel);

        player.drawCards(gameDeck);
    }

    public Enemy createEnemy(int level) {
        return enemyPool.getRandomEnemyForLevel(level);
    }

    public void completeEncounter(boolean enemyDefeated) {
        if (enemyDefeated) {
            player.addGold(calculateGoldReward());

            currentLevel++;
        }
    }

    // New method to check if a shop visit should happen
    public boolean shouldVisitShop() {
        // Check if current level (before incrementing) is in the shop levels array
        for (int shopLevel : SHOP_LEVELS) {
            if (currentLevel == shopLevel) {
                return true;
            }
        }
        return false;
    }

    public boolean isGameComplete() {
        return currentLevel > FINAL_BOSS_LEVEL;
    }

    private int calculateGoldReward() {
        return GameConfig.BASE_GOLD_REWARD + (currentLevel * GameConfig.GOLD_REWARD_MULTIPLIER);
    }

    public Enemy getCurrentEnemy() {
        return currentEnemy;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }
}