package main.java.fr.ynov.ortalab.factories;

import main.java.fr.ynov.ortalab.domain.game.Item;
import main.java.fr.ynov.ortalab.domain.card.CardSuit;
import main.java.fr.ynov.ortalab.domain.HandType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class ItemFactory {
    private static final Map<String, Supplier<Item>> itemRegistry = new HashMap<>();
    private static final List<String> commonItems = new ArrayList<>();
    private static final List<String> uncommonItems = new ArrayList<>();
    private static final List<String> rareItems = new ArrayList<>();
    private static final List<String> legendaryItems = new ArrayList<>();
    private static final Random random = new Random();

    static {
        // Register all items
        registerItems();

        // Categorize items by rarity for easier random selection
        categorizeItemsByRarity();
    }

    private static void registerItems() {
        // Suit damage items
        registerItem("The Moon", () -> new Item.Builder("The Moon", Item.ItemType.SUIT_DAMAGE)
                .description("Increases damage of Clubs cards by 15")
                .rarity(Item.ItemRarity.UNCOMMON)
                .value(15)
                .buyValue(6)
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

        registerItem("The Sun", () -> new Item.Builder("The Sun", Item.ItemType.SUIT_DAMAGE)
                .description("Increases damage of Diamonds cards by 15")
                .rarity(Item.ItemRarity.UNCOMMON)
                .value(15)
                .buyValue(6)
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

        registerItem("The Star", () -> new Item.Builder("The Star", Item.ItemType.SUIT_DAMAGE)
                .description("Increases damage of Hearts cards by 15")
                .rarity(Item.ItemRarity.UNCOMMON)
                .value(15)
                .buyValue(6)
                .effect(
                        player -> player.getSuitDamageBonus().put(CardSuit.HEARTS,
                                player.getSuitDamageBonus().getOrDefault(CardSuit.HEARTS, 0) + 15),
                        player -> {
                            Map<CardSuit, Integer> bonuses = player.getSuitDamageBonus();
                            bonuses.put(CardSuit.HEARTS, bonuses.getOrDefault(CardSuit.HEARTS, 0) - 15);
                            if (bonuses.get(CardSuit.HEARTS) <= 0) bonuses.remove(CardSuit.HEARTS);
                        }
                )
                .build());

        registerItem("The World", () -> new Item.Builder("The World", Item.ItemType.SUIT_DAMAGE)
                .description("Increases damage of Spades cards by 15")
                .rarity(Item.ItemRarity.UNCOMMON)
                .value(15)
                .buyValue(6)
                .effect(
                        player -> player.getSuitDamageBonus().put(CardSuit.SPADES,
                                player.getSuitDamageBonus().getOrDefault(CardSuit.SPADES, 0) + 15),
                        player -> {
                            Map<CardSuit, Integer> bonuses = player.getSuitDamageBonus();
                            bonuses.put(CardSuit.SPADES, bonuses.getOrDefault(CardSuit.SPADES, 0) - 15);
                            if (bonuses.get(CardSuit.SPADES) <= 0) bonuses.remove(CardSuit.SPADES);
                        }
                )
                .build());

        // Hand type damage items
        registerItem("Flow", () -> new Item.Builder("Flow", Item.ItemType.HAND_TYPE_DAMAGE)
                .description("Increases damage of Flush hands by 100")
                .rarity(Item.ItemRarity.RARE)
                .value(100)
                .buyValue(8)
                .effect(
                        player -> player.getHandTypeDamageBonus().put(HandType.FLUSH,
                                player.getHandTypeDamageBonus().getOrDefault(HandType.FLUSH, 0) + 100),
                        player -> {
                            Map<HandType, Integer> bonuses = player.getHandTypeDamageBonus();
                            bonuses.put(HandType.FLUSH, bonuses.getOrDefault(HandType.FLUSH, 0) - 100);
                            if (bonuses.get(HandType.FLUSH) <= 0) bonuses.remove(HandType.FLUSH);
                        }
                )
                .build());

        registerItem("Royal Blood", () -> new Item.Builder("Royal Blood", Item.ItemType.HAND_TYPE_DAMAGE)
                .description("Increases damage of Royal Flush hands by 200")
                .rarity(Item.ItemRarity.LEGENDARY)
                .value(200)
                .buyValue(15)
                .effect(
                        player -> player.getHandTypeDamageBonus().put(HandType.ROYAL_FLUSH,
                                player.getHandTypeDamageBonus().getOrDefault(HandType.ROYAL_FLUSH, 0) + 200),
                        player -> {
                            Map<HandType, Integer> bonuses = player.getHandTypeDamageBonus();
                            bonuses.put(HandType.ROYAL_FLUSH, bonuses.getOrDefault(HandType.ROYAL_FLUSH, 0) - 200);
                            if (bonuses.get(HandType.ROYAL_FLUSH) <= 0) bonuses.remove(HandType.ROYAL_FLUSH);
                        }
                )
                .build());

        registerItem("Straight Shooter", () -> new Item.Builder("Straight Shooter", Item.ItemType.HAND_TYPE_DAMAGE)
                .description("Increases damage of Straight hands by 75")
                .rarity(Item.ItemRarity.UNCOMMON)
                .value(75)
                .buyValue(7)
                .effect(
                        player -> player.getHandTypeDamageBonus().put(HandType.STRAIGHT,
                                player.getHandTypeDamageBonus().getOrDefault(HandType.STRAIGHT, 0) + 75),
                        player -> {
                            Map<HandType, Integer> bonuses = player.getHandTypeDamageBonus();
                            bonuses.put(HandType.STRAIGHT, bonuses.getOrDefault(HandType.STRAIGHT, 0) - 75);
                            if (bonuses.get(HandType.STRAIGHT) <= 0) bonuses.remove(HandType.STRAIGHT);
                        }
                )
                .build());

        // Health items
        registerItem("Health Potion", () -> new Item.Builder("Health Potion", Item.ItemType.HEALTH_BOOST)
                .description("Restores 20 HP immediately")
                .rarity(Item.ItemRarity.COMMON)
                .value(20)
                .buyValue(5)
                .effect(
                        player -> player.heal(20),
                        player -> {} // No removal effect for one-time use items
                )
                .build());

        registerItem("Heart Amulet", () -> new Item.Builder("Heart Amulet", Item.ItemType.HEALTH_BOOST)
                .description("Increases max HP by 15")
                .rarity(Item.ItemRarity.RARE)
                .value(15)
                .buyValue(10)
                .effect(
                        player -> {
                            // Assume there's a method to increase max HP
                            // player.increaseMaxHP(15);
                            player.heal(15);
                        },
                        player -> {
                            // This would need to be implemented in the Player class
                            // player.decreaseMaxHP(15);
                        }
                )
                .build());

        // Critical chance items
        registerItem("Lucky Coin", () -> new Item.Builder("Lucky Coin", Item.ItemType.CRITICAL_CHANCE)
                .description("Increases critical hit chance by 5%")
                .rarity(Item.ItemRarity.COMMON)
                .value(5)
                .buyValue(4)
                .effect(
                        player -> {
                            // Increase critical chance by 5%
                            // This would need to be implemented in the Player class
                            // player.increaseCriticalChance(5);
                        },
                        player -> {
                            // Decrease critical chance by 5%
                            // player.decreaseCriticalChance(5);
                        }
                )
                .build());

        // Discard boost items
        registerItem("Dealer's Trick", () -> new Item.Builder("Dealer's Trick", Item.ItemType.DISCARD_BOOST)
                .description("Increases max discards by 1")
                .rarity(Item.ItemRarity.UNCOMMON)
                .value(1)
                .buyValue(7)
                .effect(
                        player -> {
                            // Increase max discards
                            // This would need to be implemented in the Player class
                            // player.increaseMaxDiscards(1);
                        },
                        player -> {
                            // Decrease max discards
                            // player.decreaseMaxDiscards(1);
                        }
                )
                .build());

        // Add many more items following this pattern...
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
                case UNCOMMON:
                    uncommonItems.add(itemName);
                    break;
                case RARE:
                    rareItems.add(itemName);
                    break;
                case LEGENDARY:
                    legendaryItems.add(itemName);
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

            if (roll < 0.01 && !legendaryItems.isEmpty()) {
                itemName = legendaryItems.get(random.nextInt(legendaryItems.size()));
            } else if (roll < 0.10 && !rareItems.isEmpty()) {
                itemName = rareItems.get(random.nextInt(rareItems.size()));
            } else if (roll < 0.40 && !uncommonItems.isEmpty()) {
                itemName = uncommonItems.get(random.nextInt(uncommonItems.size()));
            } else {
                itemName = commonItems.get(random.nextInt(commonItems.size()));
            }

            selectedItems.add(getItem(itemName));
        }

        return selectedItems;
    }

    public static List<String> getItemNames() {
        return new ArrayList<>(itemRegistry.keySet());
    }
}