package main.java.fr.ynov.ortalab.domain.game;

import main.java.fr.ynov.ortalab.config.GameConfig;

import java.util.function.Consumer;

public class Item {
    private final String name;
    private final String description;
    private final ItemRarity rarity;
    private final int value;
    private final int buyValue;
    private final int sellValue;
    private final Consumer<Player> applyEffect;
    private final Consumer<Player> removeEffect;

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

    public void applyTo(Player player) {
        if (applyEffect != null) {
            applyEffect.accept(player);
        }
    }

    public void removeFrom(Player player) {
        if (removeEffect != null) {
            removeEffect.accept(player);
        }
    }

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

    public static class Builder {
        private final String name;
        private String description;
        private ItemRarity rarity = ItemRarity.COMMON;
        private int value;
        private int buyValue;
        private int sellValue;
        private Consumer<Player> applyEffect;
        private Consumer<Player> removeEffect;

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

        public Builder effect(Consumer<Player> applyEffect, Consumer<Player> removeEffect) {
            this.applyEffect = applyEffect;
            this.removeEffect = removeEffect;
            return this;
        }

        public Item build() {
            return new Item(this);
        }
    }
}
