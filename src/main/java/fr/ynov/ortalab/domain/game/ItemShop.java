package main.java.fr.ynov.ortalab.domain.game;

import main.java.fr.ynov.ortalab.factories.ItemFactory;

import java.util.List;

public class ItemShop {
    private static final int DEFAULT_SHOP_SIZE = 3;

    public ItemShop() {
        refreshShop();
    }

    public void refreshShop() {
        List<Item> availableItems = ItemFactory.getRandomItems(DEFAULT_SHOP_SIZE);
    }

    public List<Item> getRandomItems(int count) {
        return ItemFactory.getRandomItems(count);
    }

}