package main.java.fr.ynov.ortalab.domain.game;

public class Item {
    private final String name;
    private final ItemType type;
    private final int value;
    private final int buyValue;
    private final int sellValue;

    /**
     * Constructor for an Item
     *
     * @param name Name of the item
     * @param type Type of item effect
     * @param value Numeric value of the effect
     * @param buyValue Cost to purchase the item
     */
    public Item(String name, ItemType type, int value, int buyValue) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.buyValue = buyValue;
        this.sellValue = buyValue / 2; // Sell value is half the buy value
    }

    /**
     * Enumeration of possible item types
     */
    public enum ItemType {
        CRITICAL_CHANCE,      // Increases critical hit chance
        HEART_CARD_DAMAGE,    // Increases damage for Heart cards
        EXTRA_DISCARD,        // Increases number of discards
        HEALTH_BONUS,         // Increases max health
        DAMAGE_REDUCTION,     // Reduces incoming damage
        GOLD_BONUS            // Increases gold from victories
    }

    // Getters
    public String getName() {
        return name;
    }

    public ItemType getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public int getBuyValue() {
        return buyValue;
    }

    public int getSellValue() {
        return sellValue;
    }
}