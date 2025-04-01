package main.java.fr.ynov.ortalab.factories;

import main.java.fr.ynov.ortalab.domain.game.Enemy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EnemyFactory {
    private static final Random random = new Random();
    private final Map<Integer, List<EnemyType>> enemyTypes = new HashMap<>();

    public EnemyFactory() {
        initializeEnemyTypes();
    }

    private void initializeEnemyTypes() {
        // Level 1 Enemies
        addEnemyType(1, new EnemyType(200, 3, 2));
        addEnemyType(1, new EnemyType(225, 2, 2));

        // Level 2 Enemies
        addEnemyType(2, new EnemyType(300, 10, 3));
        addEnemyType(2, new EnemyType(350, 6, 2));

        // Level 3 Enemies
        addEnemyType(3, new EnemyType(400, 15, 4));
        addEnemyType(3, new EnemyType(450, 12, 3));

        // Level 4 Enemies
        addEnemyType(4, new EnemyType(300, 3, 1));
        addEnemyType(4, new EnemyType(525, 20, 5));

        // Level 5 (Final Boss)
        addEnemyType(5, new EnemyType(1200, 50, 5));
    }

    private void addEnemyType(int level, EnemyType enemyType) {
        enemyTypes.computeIfAbsent(level, k -> new ArrayList<>()).add(enemyType);
    }

    public Enemy createEnemyForLevel(int level) {
        List<EnemyType> levelEnemyTypes = enemyTypes.get(level);
        if (levelEnemyTypes == null || levelEnemyTypes.isEmpty()) {
            throw new IllegalArgumentException("No enemy types found for level " + level);
        }
        EnemyType selectedType = levelEnemyTypes.get(random.nextInt(levelEnemyTypes.size()));
        return new Enemy(selectedType.health(), selectedType.attackDamage(), selectedType.cooldown());
    }

    // Inner class to define enemy templates
        private record EnemyType(int health, int attackDamage, int cooldown) {
    }
}