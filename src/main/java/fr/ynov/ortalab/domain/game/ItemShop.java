package main.java.fr.ynov.ortalab.domain.game;

import main.java.fr.ynov.ortalab.factories.ItemFactory;

import java.util.ArrayList;
import java.util.List;

public class ItemShop {
    private List<Item> availableItems;
    private static final int DEFAULT_SHOP_SIZE = 3;

    public ItemShop() {
        refreshShop();
    }

    public void refreshShop() {
        availableItems = ItemFactory.getRandomItems(DEFAULT_SHOP_SIZE);
    }

    public List<Item> getAvailableItems() {
        return new ArrayList<>(availableItems);
    }

    public List<Item> getRandomItems(int count) {
        return ItemFactory.getRandomItems(count);
    }

    public boolean purchaseItem(Player player, Item item) {
        if (!availableItems.contains(item)) {
            System.out.println("Item not found in shop");
            return false;
        }

        if (player.getGold() < item.getBuyValue()) {
            System.out.println("Not enough gold: " + player.getGold() + " < " + item.getBuyValue());
            return false;
        }

        int currentGold = player.getGold();
        player.addGold(-item.getBuyValue());

        System.out.println("Gold before: " + currentGold + ", After: " + player.getGold());

        if (player.addItem(item)) {
            availableItems.remove(item);
            return true;
        } else {
            player.addGold(item.getBuyValue());
            return false;
        }
    }
}