package main.java.fr.ynov.ortalab.factories;

import main.java.fr.ynov.ortalab.config.GameConfig;
import main.java.fr.ynov.ortalab.domain.game.Item;
import main.java.fr.ynov.ortalab.domain.card.CardSuit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class ItemFactory {
    private static final Map<String, Supplier<Item>> itemRegistry = new HashMap<>();
    private static final List<String> commonItems = new ArrayList<>();
    private static final List<String> rareItems = new ArrayList<>();
    private static final Random random = new Random();

    static {
        registerItems();
        categorizeItemsByRarity();
    }

    private static void registerItems() {
        // Suit damage items
        registerItem("The Moon", () -> new Item.Builder("The Moon")
                .description("Increases damage of Clubs cards by 15")
                .rarity(Item.ItemRarity.COMMON)
                .value(15)
                .buyValue(GameConfig.ITEM_SELL_PRICE)
                .effect(
                        player -> player.getSuitDamageBonus().put(CardSuit.CLUBS,
                                player.getSuitDamageBonus().getOrDefault(CardSuit.CLUBS, 0) + 15),
                        player -> {
                            Map<CardSuit, Integer> bonuses = player.getSuitDamageBonus();
                            bonuses.put(CardSuit.CLUBS, bonuses.getOrDefault(CardSuit.CLUBS, 0) - 15);
                            if (bonuses.get(CardSuit.CLUBS) <= 0) bonuses.remove(CardSuit.CLUBS);
                        }
                )
                .build());

        registerItem("The Sun", () -> new Item.Builder("The Sun")
                .description("Increases damage of Diamonds cards by 15")
                .rarity(Item.ItemRarity.COMMON)
                .value(15)
                .buyValue(GameConfig.ITEM_SELL_PRICE)
                .effect(
                        player -> player.getSuitDamageBonus().put(CardSuit.DIAMONDS,
                                player.getSuitDamageBonus().getOrDefault(CardSuit.DIAMONDS, 0) + 15),
                        player -> {
                            Map<CardSuit, Integer> bonuses = player.getSuitDamageBonus();
                            bonuses.put(CardSuit.DIAMONDS, bonuses.getOrDefault(CardSuit.DIAMONDS, 0) - 15);
                            if (bonuses.get(CardSuit.DIAMONDS) <= 0) bonuses.remove(CardSuit.DIAMONDS);
                        }
                )
                .build());

        registerItem("Isolation", () -> new Item.Builder("Isolation")
                .description("High Card Attacks deal +40 damage.")
                .rarity(Item.ItemRarity.COMMON)
                .value(40)
                .buyValue(GameConfig.ITEM_SELL_PRICE)
                .effect(
                        player -> player.getSuitDamageBonus().put(CardSuit.DIAMONDS,
                                player.getSuitDamageBonus().getOrDefault(CardSuit.DIAMONDS, 0) + 15),
                        player -> {
                            Map<CardSuit, Integer> bonuses = player.getSuitDamageBonus();
                            bonuses.put(CardSuit.DIAMONDS, bonuses.getOrDefault(CardSuit.DIAMONDS, 0) - 15);
                            if (bonuses.get(CardSuit.DIAMONDS) <= 0) bonuses.remove(CardSuit.DIAMONDS);
                        }
                )
                .build());
    }

    private static void registerItem(String itemName, Supplier<Item> itemSupplier) {
        itemRegistry.put(itemName, itemSupplier);
    }

    private static void categorizeItemsByRarity() {
        for (String itemName : itemRegistry.keySet()) {
            Item item = getItem(itemName);
            switch (item.getRarity()) {
                case COMMON:
                    commonItems.add(itemName);
                    break;
                case RARE:
                    rareItems.add(itemName);
                    break;
            }
        }
    }

    public static Item getItem(String itemName) {
        Supplier<Item> itemSupplier = itemRegistry.get(itemName);
        if (itemSupplier == null) {
            throw new IllegalArgumentException("No item registered with name: " + itemName);
        }
        return itemSupplier.get();
    }

    public static List<Item> getRandomItems(int count) {
        List<Item> selectedItems = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            float roll = random.nextFloat();
            String itemName;

            if (roll < Item.ItemRarity.RARE.getDropRate() && !rareItems.isEmpty()) {
                itemName = rareItems.get(random.nextInt(rareItems.size()));
            } else {
                itemName = commonItems.get(random.nextInt(commonItems.size()));
            }

            selectedItems.add(getItem(itemName));
        }

        return selectedItems;
    }
}