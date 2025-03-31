package main.java.fr.ynov.ortalab.domain.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemShop {
    private List<Item> availableItems;
    private static final Random random = new Random();
    private static final int DEFAULT_SHOP_SIZE = 3;

    public ItemShop() {
        initializeShop();
    }

    private void initializeShop() {
        availableItems = new ArrayList<>();

        // Add core items that should always be available
        availableItems.add(createTheMoonItem());
        availableItems.add(createTheSunItem());
        availableItems.add(createFlowItem());

        // You can add more items here as you develop them
    }

    public Item createTheMoonItem() {
        return new Item("The Moon", Item.ItemType.SUIT_DAMAGE, 15, 6);
    }

    public Item createTheSunItem() {
        return new Item("The Sun", Item.ItemType.SUIT_DAMAGE, 15, 6);
    }

    public Item createFlowItem() {
        return new Item("Flow", Item.ItemType.HAND_TYPE_DAMAGE, 100, 8);
    }

    public List<Item> getAvailableItems() {
        return new ArrayList<>(availableItems);
    }

    public List<Item> getRandomItems(int count) {
        List<Item> shuffledItems = new ArrayList<>(availableItems);
        java.util.Collections.shuffle(shuffledItems, random);

        int itemCount = Math.min(count, shuffledItems.size());
        return shuffledItems.subList(0, itemCount);
    }

    public boolean purchaseItem(Player player, Item item) {
        // Check if the item is available in the shop
        if (!availableItems.contains(item)) {
            System.out.println("Item not found in shop");
            return false;
        }

        // Check if player has enough gold
        if (player.getGold() < item.getBuyValue()) {
            System.out.println("Not enough gold: " + player.getGold() + " < " + item.getBuyValue());
            return false;
        }

        // Directly deduct gold first
        int currentGold = player.getGold();
        player.addGold(-item.getBuyValue());

        // Debug output
        System.out.println("Gold before: " + currentGold + ", After: " + player.getGold());

        // Then try to add the item
        if (player.addItem(item)) {
            return true;
        } else {
            // If adding item fails, refund the gold
            player.addGold(item.getBuyValue());
            return false;
        }
    }
}