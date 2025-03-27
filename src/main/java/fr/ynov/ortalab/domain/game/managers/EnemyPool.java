package main.java.fr.ynov.ortalab.domain.game.managers;

import main.java.fr.ynov.ortalab.domain.game.Enemy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EnemyPool {
    private static final Random random = new Random();
    private Map<Integer, List<Enemy>> enemyLevels = new HashMap<>();

    public EnemyPool() {
        initializeEnemyPools();
    }

    private void initializeEnemyPools() {
        // Level 1 Enemies
        addEnemy(1, new Enemy(200, 3, 2));
        addEnemy(1, new Enemy(225, 2, 2));

        // Level 2 Enemies
        addEnemy(2, new Enemy(300, 10, 3));
        addEnemy(2, new Enemy(350, 6, 2));

        // Level 3 Enemies
        addEnemy(3, new Enemy(400, 15, 4));
        addEnemy(3, new Enemy(450, 12, 3));

        // Level 4 Enemies
        addEnemy(4, new Enemy(300, 3, 1));
        addEnemy(4, new Enemy(525, 20, 5));

        // Level 5 (Final Boss)
        addEnemy(5, new Enemy(1200, 50, 5));
    }

    private void addEnemy(int level, Enemy enemy) {
        enemyLevels.computeIfAbsent(level, k -> new ArrayList<>()).add(enemy);
    }

    public Enemy getRandomEnemyForLevel(int level) {
        List<Enemy> levelEnemies = enemyLevels.get(level);
        if (levelEnemies == null || levelEnemies.isEmpty()) {
            throw new IllegalArgumentException("No enemies found for level " + level);
        }
        return levelEnemies.get(random.nextInt(levelEnemies.size()));
    }
}