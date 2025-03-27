package main.java.fr.ynov.ortalab.config;

public class GameConfig {
    public static final int INITIAL_PLAYER_HP = 100;
    public static final int MAX_HAND_SIZE = 8;
    public static final int ACTIVE_HAND_SIZE = 5;
    public static final int MAX_DISCARDS_PER_ENEMY = 3;
    public static final int INITIAL_CRITICAL_CHANCE = 5;
    public static final int MAX_INVENTORY_SIZE = 6;

    // Game Progression Configuration
    public static final int FINAL_BOSS_LEVEL = 5;
    public static final int BASE_GOLD_REWARD = 10;
    public static final int GOLD_REWARD_MULTIPLIER = 5;

    // Prevent instantiation
    private GameConfig() {
        throw new AssertionError("Cannot instantiate configuration class");
    }
}