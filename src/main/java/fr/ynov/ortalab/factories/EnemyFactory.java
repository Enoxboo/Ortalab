package main.java.fr.ynov.ortalab.factories;

import main.java.fr.ynov.ortalab.domain.game.Enemy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Factory responsible for creating game enemies with appropriate
 * stats based on the current game level.
 * Uses a template pattern for enemy types grouped by level.
 */
public class EnemyFactory {
    private static final Random random = new Random();
    private final Map<Integer, List<EnemyType>> enemyTypes = new HashMap<>();

    /**
     * Constructor initializes the enemy type templates for all game levels.
     */
    public EnemyFactory() {
        initializeEnemyTypes();
    }

    /**
     * Creates an enemy instance with appropriate stats for the given level.
     * Randomly selects from available enemy types for that level.
     *
     * @param level The current game level
     * @return A new Enemy instance with appropriate stats
     * @throws IllegalArgumentException if no enemy types exist for the given level
     */
    public Enemy createEnemyForLevel(int level) {
        List<EnemyType> levelEnemyTypes = enemyTypes.get(level);
        if (levelEnemyTypes == null || levelEnemyTypes.isEmpty()) {
            throw new IllegalArgumentException("No enemy types found for level " + level);
        }
        EnemyType selectedType = levelEnemyTypes.get(random.nextInt(levelEnemyTypes.size()));
        return new Enemy(selectedType.health(), selectedType.attackDamage(), selectedType.cooldown());
    }

    /**
     * Helper method to add enemy type templates to the appropriate level group.
     */
    private void addEnemyType(int level, EnemyType enemyType) {
        enemyTypes.computeIfAbsent(level, k -> new ArrayList<>()).add(enemyType);
    }

    /**
     * Initializes all enemy types for each game level.
     * Enemy difficulty progressively increases with level.
     */
    private void initializeEnemyTypes() {
        // Level 1 Enemies - Introductory difficulty
        addEnemyType(1, new EnemyType(200, 3, 2));
        addEnemyType(1, new EnemyType(225, 2, 2));

        // Level 2 Enemies - Moderate difficulty
        addEnemyType(2, new EnemyType(300, 10, 3));
        addEnemyType(2, new EnemyType(350, 6, 2));

        // Level 3 Enemies - Challenging difficulty
        addEnemyType(3, new EnemyType(400, 15, 4));
        addEnemyType(3, new EnemyType(450, 12, 3));

        // Level 4 Enemies - Advanced difficulty
        addEnemyType(4, new EnemyType(300, 3, 1)); // Fast attacker, low health
        addEnemyType(4, new EnemyType(525, 20, 5)); // Heavy hitter with slow cooldown

        // Level 5 (Final Boss) - Maximum difficulty
        addEnemyType(5, new EnemyType(1200, 50, 5));
    }

    /**
     * Immutable enemy template defining the base stats for an enemy type.
     * Uses record for concise immutable data structure.
     */
    private record EnemyType(int health, int attackDamage, int cooldown) {
    }
}