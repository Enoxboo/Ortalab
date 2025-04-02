package main.java.fr.ynov.ortalab.domain.game;

import main.java.fr.ynov.ortalab.config.GameConfig;
import main.java.fr.ynov.ortalab.factories.ItemFactory;

import java.util.List;

/**
 * Represents an in-game shop for purchasing items.
 * Uses the ItemFactory to generate random items for sale.
 */
public class ItemShop {
    private static final int DEFAULT_SHOP_SIZE = GameConfig.DEFAULT_SHOP_SIZE;
    private List<Item> availableItems;

    /**
     * Creates a new shop with a random selection of items.
     */
    public ItemShop() {
        refreshShop();
    }

    /**
     * Refreshes the shop with a new selection of random items.
     * Called at creation and when the shop inventory needs to be changed.
     */
    public void refreshShop() {
        availableItems = ItemFactory.getRandomItems(DEFAULT_SHOP_SIZE);
    }

    /**
     * Get a specific number of random items from the factory.
     * Useful for special shop events or rewards.
     *
     * @param count Number of items to generate
     * @return List of randomly generated items
     */
    public List<Item> getRandomItems(int count) {
        return ItemFactory.getRandomItems(count);
    }

    /**
     * @return The current items available in the shop
     */
    public List<Item> getAvailableItems() {
        return availableItems;
    }
}