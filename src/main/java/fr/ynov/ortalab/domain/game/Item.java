package main.java.fr.ynov.ortalab.domain.game;

import java.util.function.Consumer;

/**
 * Represents an equippable item that provides bonuses to the player.
 * Uses the Builder pattern for flexible item creation.
 */
public class Item {
    private final String name;
    private final String description;
    private final ItemRarity rarity;
    private final int value;
    private final int buyValue;
    private final int sellValue;
    private final Consumer<Player> applyEffect;
    private final Consumer<Player> removeEffect;

    /**
     * Defines item rarity levels with associated drop rates.
     * Higher rarity items are less likely to drop.
     */
    public enum ItemRarity {
        TEST(1.0f),
        COMMON(0.7f),
        RARE(0.3f);

        private final float dropRate;

        ItemRarity(float dropRate) {
            this.dropRate = dropRate;
        }

        public float getDropRate() {
            return dropRate;
        }
    }

    /**
     * Private constructor that works with the Builder pattern.
     * All item creation should go through the Builder.
     *
     * @param builder The builder containing item configuration
     */
    private Item(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.rarity = builder.rarity;
        this.value = builder.value;
        this.buyValue = builder.buyValue;
        this.sellValue = builder.sellValue;
        this.applyEffect = builder.applyEffect;
        this.removeEffect = builder.removeEffect;
    }

    /**
     * Apply this item's effect to a player.
     * Called when an item is added to inventory.
     *
     * @param player The player to apply effects to
     */
    public void applyTo(Player player) {
        if (applyEffect != null) {
            applyEffect.accept(player);
        }
    }

    /**
     * Remove this item's effect from a player.
     * Called when an item is sold or removed.
     *
     * @param player The player to remove effects from
     */
    public void removeFrom(Player player) {
        if (removeEffect != null) {
            removeEffect.accept(player);
        }
    }

    // ==================== GETTERS ====================

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ItemRarity getRarity() {
        return rarity;
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

    /**
     * Builder class for creating Item instances with a fluent API.
     * Enforces required name field and provides defaults for optional fields.
     */
    public static class Builder {
        private final String name;
        private String description;
        private ItemRarity rarity = ItemRarity.COMMON;
        private int value;
        private int buyValue;
        private int sellValue;
        private Consumer<Player> applyEffect;
        private Consumer<Player> removeEffect;

        /**
         * Create a new builder with the required item name.
         *
         * @param name The name of the item (required)
         */
        public Builder(String name) {
            this.name = name;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder rarity(ItemRarity rarity) {
            this.rarity = rarity;
            return this;
        }

        public Builder value(int value) {
            this.value = value;
            return this;
        }

        public Builder buyValue(int buyValue) {
            this.buyValue = buyValue;
            return this;
        }

        public Builder sellValue(int sellValue) {
            this.sellValue = sellValue;
            return this;
        }

        /**
         * Set the effect functions for this item.
         *
         * @param applyEffect Function called when item is equipped
         * @param removeEffect Function called when item is removed
         * @return This builder for method chaining
         */
        public Builder effect(Consumer<Player> applyEffect, Consumer<Player> removeEffect) {
            this.applyEffect = applyEffect;
            this.removeEffect = removeEffect;
            return this;
        }

        /**
         * Build and return the final Item instance.
         *
         * @return The configured Item
         */
        public Item build() {
            return new Item(this);
        }
    }
}