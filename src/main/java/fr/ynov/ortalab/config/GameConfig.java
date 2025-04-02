package main.java.fr.ynov.ortalab.config;

/**
 * Contains game configuration constants that define core game mechanics.
 * This class is not meant to be instantiated.
 */
public class GameConfig {
    public static final int INITIAL_PLAYER_HP = 100;
    public static final int MAX_HAND_SIZE = 8;
    public static final int ACTIVE_HAND_SIZE = 5;
    public static final int MAX_DISCARDS_PER_ENEMY = 3;
    public static final int MAX_INVENTORY_SIZE = 6;
    public static final int ITEM_SELL_PRICE = 2;
    public static final int DEFAULT_SHOP_SIZE = 3;
    public static final int FINAL_BOSS_LEVEL = 5;
    public static final int BASE_GOLD_REWARD = 5;
    public static final int GOLD_REWARD_MULTIPLIER = 5;
    public static final int[] SHOP_LEVELS = {1, 3};

    /**
     * Private constructor to prevent instantiation.
     * @throws AssertionError if instantiation is attempted
     */
    private GameConfig() {
        throw new AssertionError("Cannot instantiate configuration class");
    }
}