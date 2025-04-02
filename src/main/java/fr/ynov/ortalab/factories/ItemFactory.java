package main.java.fr.ynov.ortalab.factories;

import main.java.fr.ynov.ortalab.config.GameConfig;
import main.java.fr.ynov.ortalab.domain.game.HandType;
import main.java.fr.ynov.ortalab.domain.game.PointsCalculator;
import main.java.fr.ynov.ortalab.domain.game.Item;
import main.java.fr.ynov.ortalab.domain.card.CardSuit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

/**
 * Factory responsible for creating, managing, and providing game items.
 * Implements a registry pattern for items and handles random item generation
 * based on rarity tiers.
 */
public class ItemFactory {
    private static final Map<String, Supplier<Item>> itemRegistry = new HashMap<>();
    private static final List<String> commonItems = new ArrayList<>();
    private static final List<String> rareItems = new ArrayList<>();
    private static final List<String> testItems = new ArrayList<>();
    private static final Random random = new Random();

    // Initialize items on class load
    static {
        registerItems();
        categorizeItemsByRarity();
    }

    /**
     * Retrieves an item by its name from the registry.
     *
     * @param itemName The name of the item to retrieve
     * @return A new instance of the requested item
     * @throws IllegalArgumentException if no item with the given name exists
     */
    public static Item getItem(String itemName) {
        Supplier<Item> itemSupplier = itemRegistry.get(itemName);
        if (itemSupplier == null) {
            throw new IllegalArgumentException("No item registered with name: " + itemName);
        }
        return itemSupplier.get();
    }

    /**
     * Generates a list of random items based on configured rarity drop rates.
     * Test items always have 100% chance to be included.
     *
     * @param count The number of items to generate
     * @return A list of randomly selected items
     */
    public static List<Item> getRandomItems(int count) {
        List<Item> selectedItems = new ArrayList<>();

        // First, add all TEST items (100% chance)
        if (!testItems.isEmpty()) {
            for (String itemName : testItems) {
                selectedItems.add(getItem(itemName));
                if (selectedItems.size() >= count) {
                    return selectedItems;
                }
            }
        }

        // Fill remaining slots with random items based on rarity chance
        int remainingSlots = count - selectedItems.size();
        for (int i = 0; i < remainingSlots; i++) {
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

    /**
     * Helper method to register an item in the item registry.
     */
    private static void registerItem(String itemName, Supplier<Item> itemSupplier) {
        itemRegistry.put(itemName, itemSupplier);
    }

    /**
     * Categorizes all registered items into their respective rarity lists.
     */
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
                case TEST:
                    testItems.add(itemName);
                    break;
            }
        }
    }

    /**
     * Registers all game items with their effects, values, and descriptions.
     * Items are grouped by their effect types for better organization.
     */
    private static void registerItems() {
        // === SUIT DAMAGE BONUS ITEMS ===
        registerItem("The Moon", () -> new Item.Builder("The Moon")
                .description("Increases damage of Clubs cards by 15")
                .rarity(Item.ItemRarity.COMMON)
                .value(15)
                .sellValue(GameConfig.ITEM_SELL_PRICE)
                .buyValue(6)
                .effect(
                        player -> player.getSuitDamageBonus().put(CardSuit.CLUBS,
                                player.getSuitDamageBonus().getOrDefault(CardSuit.CLUBS, 0) + getItem("The Moon").getValue()),
                        player -> {
                            Map<CardSuit, Integer> bonuses = player.getSuitDamageBonus();
                            bonuses.put(CardSuit.CLUBS, bonuses.getOrDefault(CardSuit.CLUBS, 0) - getItem("The Moon").getValue());
                            if (bonuses.get(CardSuit.CLUBS) <= 0) bonuses.remove(CardSuit.CLUBS);
                        }
                )
                .build());

        registerItem("The Sun", () -> new Item.Builder("The Sun")
                .description("Increases damage of Diamonds cards by 15")
                .rarity(Item.ItemRarity.COMMON)
                .value(15)
                .sellValue(GameConfig.ITEM_SELL_PRICE)
                .buyValue(6)
                .effect(
                        player -> player.getSuitDamageBonus().put(CardSuit.DIAMONDS,
                                player.getSuitDamageBonus().getOrDefault(CardSuit.DIAMONDS, 0) + getItem("The Sun").getValue()),
                        player -> {
                            Map<CardSuit, Integer> bonuses = player.getSuitDamageBonus();
                            bonuses.put(CardSuit.DIAMONDS, bonuses.getOrDefault(CardSuit.DIAMONDS, 0) - getItem("The Sun").getValue());
                            if (bonuses.get(CardSuit.DIAMONDS) <= 0) bonuses.remove(CardSuit.DIAMONDS);
                        }
                )
                .build());

        // === HAND TYPE BONUS ITEMS - COMMON TIER ===
        registerItem("Isolation", () -> new Item.Builder("Isolation")
                .description("High Card Attacks deal +40 damage.")
                .rarity(Item.ItemRarity.COMMON)
                .value(40)
                .sellValue(GameConfig.ITEM_SELL_PRICE)
                .buyValue(6)
                .effect(
                        player -> player.getHandTypeDamageBonus().put(HandType.HIGH_CARD,
                                player.getHandTypeDamageBonus().getOrDefault(HandType.HIGH_CARD, 0) + getItem("Isolation").getValue()),
                        player -> {
                            Map<HandType, Integer> bonuses = player.getHandTypeDamageBonus();
                            bonuses.put(HandType.HIGH_CARD, bonuses.getOrDefault(HandType.HIGH_CARD, 0) - getItem("Isolation").getValue());
                            if (bonuses.get(HandType.HIGH_CARD) <= 0) bonuses.remove(HandType.HIGH_CARD);
                        }
                )
                .build());

        registerItem("Harmony", () -> new Item.Builder("Harmony")
                .description("Full House Attacks deal +150 damage.")
                .rarity(Item.ItemRarity.COMMON)
                .value(150)
                .sellValue(GameConfig.ITEM_SELL_PRICE)
                .buyValue(7)
                .effect(
                        player -> player.getHandTypeDamageBonus().put(HandType.FULL_HOUSE,
                                player.getHandTypeDamageBonus().getOrDefault(HandType.FULL_HOUSE, 0) + getItem("Harmony").getValue()),
                        player -> {
                            Map<HandType, Integer> bonuses = player.getHandTypeDamageBonus();
                            bonuses.put(HandType.FULL_HOUSE, bonuses.getOrDefault(HandType.FULL_HOUSE, 0) - getItem("Harmony").getValue());
                            if (bonuses.get(HandType.FULL_HOUSE) <= 0) bonuses.remove(HandType.FULL_HOUSE);
                        }
                )
                .build());

        registerItem("Alignment", () -> new Item.Builder("Alignment")
                .description("Attacks containing a Straight deal +70 damage.")
                .rarity(Item.ItemRarity.COMMON)
                .value(70)
                .sellValue(GameConfig.ITEM_SELL_PRICE)
                .buyValue(7)
                .effect(
                        player -> player.getHandTypeDamageBonus().put(HandType.STRAIGHT,
                                player.getHandTypeDamageBonus().getOrDefault(HandType.STRAIGHT, 0) + getItem("Alignment").getValue()),
                        player -> {
                            Map<HandType, Integer> bonuses = player.getHandTypeDamageBonus();
                            bonuses.put(HandType.STRAIGHT, bonuses.getOrDefault(HandType.STRAIGHT, 0) - getItem("Alignment").getValue());
                            if (bonuses.get(HandType.STRAIGHT) <= 0) bonuses.remove(HandType.STRAIGHT);
                        }
                )
                .build());

        registerItem("Dyadic", () -> new Item.Builder("Dyadic")
                .description("Double Pairs Attacks deal +60 damage.")
                .rarity(Item.ItemRarity.COMMON)
                .value(60)
                .sellValue(GameConfig.ITEM_SELL_PRICE)
                .buyValue(6)
                .effect(
                        player -> player.getHandTypeDamageBonus().put(HandType.TWO_PAIR,
                                player.getHandTypeDamageBonus().getOrDefault(HandType.TWO_PAIR, 0) + getItem("Dyadic").getValue()),
                        player -> {
                            Map<HandType, Integer> bonuses = player.getHandTypeDamageBonus();
                            bonuses.put(HandType.TWO_PAIR, bonuses.getOrDefault(HandType.TWO_PAIR, 0) - getItem("Dyadic").getValue());
                            if (bonuses.get(HandType.TWO_PAIR) <= 0) bonuses.remove(HandType.TWO_PAIR);
                        }
                )
                .build());

        registerItem("Triadic", () -> new Item.Builder("Triadic")
                .description("Attacks containing a Three of a kind deal +85 damage.")
                .rarity(Item.ItemRarity.COMMON)
                .value(85)
                .sellValue(GameConfig.ITEM_SELL_PRICE)
                .buyValue(7)
                .effect(
                        player -> player.getHandTypeDamageBonus().put(HandType.THREE_OF_A_KIND,
                                player.getHandTypeDamageBonus().getOrDefault(HandType.THREE_OF_A_KIND, 0) + getItem("Triadic").getValue()),
                        player -> {
                            Map<HandType, Integer> bonuses = player.getHandTypeDamageBonus();
                            bonuses.put(HandType.THREE_OF_A_KIND, bonuses.getOrDefault(HandType.THREE_OF_A_KIND, 0) - getItem("Triadic").getValue());
                            if (bonuses.get(HandType.THREE_OF_A_KIND) <= 0) bonuses.remove(HandType.THREE_OF_A_KIND);
                        }
                )
                .build());

        registerItem("Flow", () -> new Item.Builder("Flow")
                .description("Attacks containing a Flush deal +100 damage.")
                .rarity(Item.ItemRarity.COMMON)
                .value(100)
                .sellValue(GameConfig.ITEM_SELL_PRICE)
                .buyValue(8)
                .effect(
                        player -> player.getHandTypeDamageBonus().put(HandType.FLUSH,
                                player.getHandTypeDamageBonus().getOrDefault(HandType.FLUSH, 0) + getItem("Flow").getValue()),
                        player -> {
                            Map<HandType, Integer> bonuses = player.getHandTypeDamageBonus();
                            bonuses.put(HandType.FLUSH, bonuses.getOrDefault(HandType.FLUSH, 0) - getItem("Flow").getValue());
                            if (bonuses.get(HandType.FLUSH) <= 0) bonuses.remove(HandType.FLUSH);
                        }
                )
                .build());

        // === SPECIAL BONUS ITEMS - COMMON TIER ===
        registerItem("The Commander", () -> new Item.Builder("The Commander")
                .description("Each Attacking honor card deals +30 damage.")
                .rarity(Item.ItemRarity.COMMON)
                .value(30)
                .sellValue(GameConfig.ITEM_SELL_PRICE)
                .buyValue(10)
                .effect(
                        player -> player.getCardValueTypeBonus().put(PointsCalculator.CardValueType.HONOR,
                                player.getCardValueTypeBonus().getOrDefault(PointsCalculator.CardValueType.HONOR, 0) + getItem("The Commander").getValue()),
                        player -> {
                            Map<PointsCalculator.CardValueType, Integer> bonuses = player.getCardValueTypeBonus();
                            bonuses.put(PointsCalculator.CardValueType.HONOR, bonuses.getOrDefault(PointsCalculator.CardValueType.HONOR, 0) - getItem("The Commander").getValue());
                            if (bonuses.get(PointsCalculator.CardValueType.HONOR) <= 0) bonuses.remove(PointsCalculator.CardValueType.HONOR);
                        }
                )
                .build());

        // === HAND TYPE BONUS ITEMS - RARE TIER ===
        registerItem("Isolation+", () -> new Item.Builder("Isolation+")
                .description("High Card Attacks deal +100 damage.")
                .rarity(Item.ItemRarity.RARE)
                .value(100)
                .sellValue(GameConfig.ITEM_SELL_PRICE)
                .buyValue(15)
                .effect(
                        player -> player.getHandTypeDamageBonus().put(HandType.HIGH_CARD,
                                player.getHandTypeDamageBonus().getOrDefault(HandType.HIGH_CARD, 0) + getItem("Isolation+").getValue()),
                        player -> {
                            Map<HandType, Integer> bonuses = player.getHandTypeDamageBonus();
                            bonuses.put(HandType.HIGH_CARD, bonuses.getOrDefault(HandType.HIGH_CARD, 0) - getItem("Isolation+").getValue());
                            if (bonuses.get(HandType.HIGH_CARD) <= 0) bonuses.remove(HandType.HIGH_CARD);
                        }
                )
                .build());

        registerItem("Harmony+", () -> new Item.Builder("Harmony+")
                .description("Full House Attacks deal +300 damage.")
                .rarity(Item.ItemRarity.RARE)
                .value(300)
                .sellValue(GameConfig.ITEM_SELL_PRICE)
                .buyValue(18)
                .effect(
                        player -> player.getHandTypeDamageBonus().put(HandType.FULL_HOUSE,
                                player.getHandTypeDamageBonus().getOrDefault(HandType.FULL_HOUSE, 0) + getItem("Harmony+").getValue()),
                        player -> {
                            Map<HandType, Integer> bonuses = player.getHandTypeDamageBonus();
                            bonuses.put(HandType.FULL_HOUSE, bonuses.getOrDefault(HandType.FULL_HOUSE, 0) - getItem("Harmony+").getValue());
                            if (bonuses.get(HandType.FULL_HOUSE) <= 0) bonuses.remove(HandType.FULL_HOUSE);
                        }
                )
                .build());

        registerItem("Dyadic+", () -> new Item.Builder("Dyadic+")
                .description("Double Pairs Attacks deal +140 damage.")
                .rarity(Item.ItemRarity.RARE)
                .value(140)
                .sellValue(GameConfig.ITEM_SELL_PRICE)
                .buyValue(16)
                .effect(
                        player -> player.getHandTypeDamageBonus().put(HandType.TWO_PAIR,
                                player.getHandTypeDamageBonus().getOrDefault(HandType.TWO_PAIR, 0) + getItem("Dyadic+").getValue()),
                        player -> {
                            Map<HandType, Integer> bonuses = player.getHandTypeDamageBonus();
                            bonuses.put(HandType.TWO_PAIR, bonuses.getOrDefault(HandType.TWO_PAIR, 0) - getItem("Dyadic+").getValue());
                            if (bonuses.get(HandType.TWO_PAIR) <= 0) bonuses.remove(HandType.TWO_PAIR);
                        }
                )
                .build());

        registerItem("Flow+", () -> new Item.Builder("Flow+")
                .description("Attacks containing a Flush deal +200 damage.")
                .rarity(Item.ItemRarity.RARE)
                .value(200)
                .sellValue(GameConfig.ITEM_SELL_PRICE)
                .buyValue(20)
                .effect(
                        player -> player.getHandTypeDamageBonus().put(HandType.FLUSH,
                                player.getHandTypeDamageBonus().getOrDefault(HandType.FLUSH, 0) + getItem("Flow+").getValue()),
                        player -> {
                            Map<HandType, Integer> bonuses = player.getHandTypeDamageBonus();
                            bonuses.put(HandType.FLUSH, bonuses.getOrDefault(HandType.FLUSH, 0) - getItem("Flow+").getValue());
                            if (bonuses.get(HandType.FLUSH) <= 0) bonuses.remove(HandType.FLUSH);
                        }
                )
                .build());

        registerItem("Alignment+", () -> new Item.Builder("Alignment+")
                .description("Attacks containing a Straight deal +140 damage.")
                .rarity(Item.ItemRarity.RARE)
                .value(140)
                .sellValue(GameConfig.ITEM_SELL_PRICE)
                .buyValue(20)
                .effect(
                        player -> player.getHandTypeDamageBonus().put(HandType.STRAIGHT,
                                player.getHandTypeDamageBonus().getOrDefault(HandType.STRAIGHT, 0) + getItem("Alignment+").getValue()),
                        player -> {
                            Map<HandType, Integer> bonuses = player.getHandTypeDamageBonus();
                            bonuses.put(HandType.STRAIGHT, bonuses.getOrDefault(HandType.STRAIGHT, 0) - getItem("Alignment+").getValue());
                            if (bonuses.get(HandType.STRAIGHT) <= 0) bonuses.remove(HandType.STRAIGHT);
                        }
                )
                .build());

        registerItem("Triadic+", () -> new Item.Builder("Triadic+")
                .description("Attacks containing a Three of a kind deal +170 damage.")
                .rarity(Item.ItemRarity.RARE)
                .value(170)
                .sellValue(GameConfig.ITEM_SELL_PRICE)
                .buyValue(20)
                .effect(
                        player -> player.getHandTypeDamageBonus().put(HandType.THREE_OF_A_KIND,
                                player.getHandTypeDamageBonus().getOrDefault(HandType.THREE_OF_A_KIND, 0) + getItem("Triadic+").getValue()),
                        player -> {
                            Map<HandType, Integer> bonuses = player.getHandTypeDamageBonus();
                            bonuses.put(HandType.THREE_OF_A_KIND, bonuses.getOrDefault(HandType.THREE_OF_A_KIND, 0) - getItem("Triadic+").getValue());
                            if (bonuses.get(HandType.THREE_OF_A_KIND) <= 0) bonuses.remove(HandType.THREE_OF_A_KIND);
                        }
                )
                .build());

        // === SPECIAL BONUS ITEMS - RARE TIER ===
        registerItem("The Swarm", () -> new Item.Builder("The Swarm")
                .description("If an Attack contains three or more cards, it deals +125 damage.")
                .rarity(Item.ItemRarity.RARE)
                .value(125)
                .sellValue(GameConfig.ITEM_SELL_PRICE)
                .buyValue(23)
                .effect(
                        player -> player.getCardCountBonus().put(3,
                                player.getCardCountBonus().getOrDefault(3, 0) + getItem("The Swarm").getValue()),
                        player -> {
                            Map<Integer, Integer> bonuses = player.getCardCountBonus();
                            bonuses.put(3, bonuses.getOrDefault(3, 0) - getItem("The Swarm").getValue());
                            if (bonuses.get(3) <= 0) bonuses.remove(3);
                        }
                )
                .build());

        registerItem("The Commander+", () -> new Item.Builder("The Commander+")
                .description("Each Attacking honor card deals +70 damage.")
                .rarity(Item.ItemRarity.RARE)
                .value(70)
                .sellValue(GameConfig.ITEM_SELL_PRICE)
                .buyValue(23)
                .effect(
                        player -> player.getCardValueTypeBonus().put(PointsCalculator.CardValueType.HONOR,
                                player.getCardValueTypeBonus().getOrDefault(PointsCalculator.CardValueType.HONOR, 0) + getItem("The Commander+").getValue()),
                        player -> {
                            Map<PointsCalculator.CardValueType, Integer> bonuses = player.getCardValueTypeBonus();
                            bonuses.put(PointsCalculator.CardValueType.HONOR, bonuses.getOrDefault(PointsCalculator.CardValueType.HONOR, 0) - getItem("The Commander+").getValue());
                            if (bonuses.get(PointsCalculator.CardValueType.HONOR) <= 0) bonuses.remove(PointsCalculator.CardValueType.HONOR);
                        }
                )
                .build());

        registerItem("Rejection Deal", () -> new Item.Builder("Rejection Deal")
                .description("+35 damage for each card used in the Attack that does not create a real hand.")
                .rarity(Item.ItemRarity.RARE)
                .value(35)
                .sellValue(GameConfig.ITEM_SELL_PRICE)
                .buyValue(20)
                .effect(
                        player -> player.setRejectionBonus(getItem("Rejection Deal").getValue()),
                        player -> player.setRejectionBonus(0)
                )
                .build());

        // === DEFENSIVE AND UTILITY ITEMS ===
        registerItem("Mediation", () -> new Item.Builder("Mediation")
                .description("Restore 10 Health after a battle ends.")
                .rarity(Item.ItemRarity.COMMON)
                .value(10)
                .sellValue(GameConfig.ITEM_SELL_PRICE)
                .buyValue(10)
                .effect(
                        player -> player.setBattleEndHealingAmount(
                                player.getBattleEndHealingAmount() + getItem("Mediation").getValue()),
                        player -> player.setBattleEndHealingAmount(
                                player.getBattleEndHealingAmount() - getItem("Mediation").getValue())
                )
                .build());

        registerItem("Protection", () -> new Item.Builder("Protection")
                .description("All damage received is reduced by 1 and further reduced by 3 while you are below 50% Health.")
                .rarity(Item.ItemRarity.COMMON)
                .value(1) // Base damage reduction
                .sellValue(GameConfig.ITEM_SELL_PRICE)
                .buyValue(12)
                .effect(
                        player -> {
                            player.setBaseDamageReduction(
                                    player.getBaseDamageReduction() + getItem("Protection").getValue());
                            player.setLowHealthDamageReduction(
                                    player.getLowHealthDamageReduction() + 3); // Additional reduction when health is low
                        },
                        player -> {
                            player.setBaseDamageReduction(
                                    player.getBaseDamageReduction() - getItem("Protection").getValue());
                            player.setLowHealthDamageReduction(
                                    player.getLowHealthDamageReduction() - 3);
                        }
                )
                .build());

        registerItem("Refusal", () -> new Item.Builder("Refusal")
                .description("Increases the number of discards per enemy by 1.")
                .rarity(Item.ItemRarity.COMMON)
                .value(1)
                .sellValue(GameConfig.ITEM_SELL_PRICE)
                .buyValue(9)
                .effect(
                        player -> player.setMaxDiscards(player.getMaxDiscards() + getItem("Refusal").getValue()),
                        player -> player.setMaxDiscards(player.getMaxDiscards() - getItem("Refusal").getValue())
                )
                .build());

        registerItem("Refusal+", () -> new Item.Builder("Refusal+")
                .description("Increases the number of discards per enemy by 2.")
                .rarity(Item.ItemRarity.RARE)
                .value(2)
                .sellValue(GameConfig.ITEM_SELL_PRICE)
                .buyValue(18)
                .effect(
                        player -> player.setMaxDiscards(player.getMaxDiscards() + getItem("Refusal+").getValue()),
                        player -> player.setMaxDiscards(player.getMaxDiscards() - getItem("Refusal+").getValue())
                )
                .build());
    }
}