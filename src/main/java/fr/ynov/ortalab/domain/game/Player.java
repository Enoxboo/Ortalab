package main.java.fr.ynov.ortalab.domain.game;

import main.java.fr.ynov.ortalab.config.GameConfig;
import main.java.fr.ynov.ortalab.domain.card.Card;
import main.java.fr.ynov.ortalab.domain.card.CardSuit;
import main.java.fr.ynov.ortalab.domain.exceptions.DeckException;
import main.java.fr.ynov.ortalab.domain.exceptions.PlayerActionException;
import main.java.fr.ynov.ortalab.domain.exceptions.CardOperationException;

import java.util.*;

/**
 * Represents a player in the card-based combat game.
 * Manages player health, inventory, card hands, and calculates damage based on poker hands.
 */
public class Player {
    // Health and economy attributes
    private int healthPoints;
    private final int maxHealthPoints;
    private int gold;

    // Card and hand management
    private int discardCount;
    private final int MAX_HAND_SIZE = GameConfig.MAX_HAND_SIZE;
    private final int ACTIVE_HAND_SIZE = GameConfig.ACTIVE_HAND_SIZE;
    private final List<Card> currentHand;
    private List<Card> selectedHand;
    private int maxDiscards = GameConfig.MAX_DISCARDS_PER_ENEMY;

    // Inventory management
    private final List<Item> inventory;

    // Combat bonuses and player stats
    private final Map<CardSuit, Integer> suitDamageBonus;
    private final Map<HandType, Integer> handTypeDamageBonus;
    private final Map<PointsCalculator.CardValueType, Integer> cardValueTypeBonus;
    private final Map<Integer, Integer> cardCountBonus;
    private int rejectionBonus;
    private int battleEndHealingAmount;
    private int baseDamageReduction;
    private int lowHealthDamageReduction;

    /**
     * Creates a new player with specified initial health.
     * Initializes all bonus maps and collections.
     *
     * @param initialHP Initial health points for the player
     */
    public Player(int initialHP) {
        this.healthPoints = initialHP;
        this.maxHealthPoints = initialHP;
        this.gold = 0;
        this.discardCount = 0;
        this.inventory = new ArrayList<>();
        this.currentHand = new ArrayList<>();
        this.selectedHand = new ArrayList<>();
        this.suitDamageBonus = new EnumMap<>(CardSuit.class);
        this.handTypeDamageBonus = new EnumMap<>(HandType.class);
        this.cardValueTypeBonus = new HashMap<>();
        this.cardCountBonus = new HashMap<>();
        this.rejectionBonus = 0;
        this.battleEndHealingAmount = 0;
        this.baseDamageReduction = 0;
        this.lowHealthDamageReduction = 0;
    }

    // ==================== CORE COMBAT METHODS ====================

    /**
     * Take damage from an enemy attack, applying any damage reduction bonuses.
     * Low health damage reduction is applied when health is below 50%.
     *
     * @param damage Amount of damage to take
     * @return Remaining health points
     */
    public int takeDamage(int damage) {
        int reducedDamage = Math.max(0, damage - baseDamageReduction);

        if (healthPoints < maxHealthPoints / 2) {
            reducedDamage = Math.max(0, reducedDamage - lowHealthDamageReduction);
        }

        this.healthPoints = Math.max(0, this.healthPoints - reducedDamage);
        return this.healthPoints;
    }

    /**
     * Heal the player without exceeding maximum health points.
     *
     * @param amount Amount of healing
     */
    public void heal(int amount) {
        this.healthPoints = Math.min(this.maxHealthPoints, this.healthPoints + amount);
    }

    /**
     * Calculate damage based on selected poker hand using the PointsCalculator.
     *
     * @return Calculated damage value
     */
    public int calculateHandDamage() {
        return PointsCalculator.calculateScore(selectedHand, this);
    }

    /**
     * Apply post-battle effects such as end-of-battle healing.
     */
    public void applyPostBattleEffects() {
        if (battleEndHealingAmount > 0) {
            heal(battleEndHealingAmount);
        }
    }

    // ==================== CARD MANAGEMENT METHODS ====================

    /**
     * Draw cards to fill the current hand up to MAX_HAND_SIZE.
     * Replenishes deck if necessary.
     *
     * @param deck The deck to draw cards from
     * @throws DeckException If drawing from the deck fails
     */
    public void drawCards(Deck deck) throws DeckException {
        currentHand.clear();

        while (currentHand.size() < MAX_HAND_SIZE) {
            deck.replenishIfEmpty();
            currentHand.add(deck.drawCard());
        }
    }

    /**
     * Select cards to form the active hand for combat.
     * Limited to ACTIVE_HAND_SIZE (typically 5) cards.
     *
     * @param selectedCards Cards chosen to form the active hand
     * @throws IllegalArgumentException if more than 5 cards are selected
     */
    public void selectHand(List<Card> selectedCards) {
        if (selectedCards.size() > ACTIVE_HAND_SIZE) {
            throw new IllegalArgumentException("Can only select up to 5 cards");
        }
        this.selectedHand = new ArrayList<>(selectedCards);
    }

    /**
     * Discard selected cards and draw replacements from the deck.
     * Limited by the maximum discard count per enemy.
     *
     * @param cardsToDiscard Cards to be discarded
     * @param deck Deck to draw new cards from
     * @throws PlayerActionException If discard action is invalid
     * @throws CardOperationException If card operations fail
     */
    public void discard(List<Card> cardsToDiscard, Deck deck) throws PlayerActionException, CardOperationException {
        validateDiscardAction(cardsToDiscard);

        try {
            deck.replenishIfEmpty();

            // Remove the specified cards from the current hand
            currentHand.removeAll(cardsToDiscard);

            // Draw replacement cards
            for (int i = 0; i < cardsToDiscard.size(); i++) {
                deck.replenishIfEmpty();
                currentHand.add(deck.drawCard());
            }

            // Ensure hand is filled to max size
            while (currentHand.size() < MAX_HAND_SIZE) {
                deck.replenishIfEmpty();
                currentHand.add(deck.drawCard());
            }

            discardCount++;
        } catch (Exception e) {
            throw new PlayerActionException("Error during card discard: " + e.getMessage(), e);
        }
    }

    /**
     * Validate that a discard action can be performed with the given cards.
     * Checks discard count limit and card selection validity.
     *
     * @param cardsToDiscard Cards selected for discard
     * @throws PlayerActionException If the discard action is invalid
     */
    private void validateDiscardAction(List<Card> cardsToDiscard) throws PlayerActionException {
        if (discardCount >= maxDiscards) {
            throw new PlayerActionException("Maximum discards for this enemy reached");
        }

        if (cardsToDiscard == null || cardsToDiscard.isEmpty()) {
            throw new PlayerActionException("No cards selected for discard");
        }

        if (cardsToDiscard.size() > ACTIVE_HAND_SIZE) {
            throw new PlayerActionException("Must discard between 1 and 5 cards");
        }
    }

    /**
     * Reset the discard counter, typically called at the start of a new battle.
     */
    public void resetDiscards() {
        this.discardCount = 0;
    }

    // ==================== INVENTORY MANAGEMENT METHODS ====================

    /**
     * Add an item to the player's inventory and apply its effects.
     * Limited by the maximum inventory size.
     *
     * @param item Item to add
     */
    public void addItem(Item item) {
        if (inventory.size() < GameConfig.MAX_INVENTORY_SIZE) {
            inventory.add(item);
            item.applyTo(this);
        }
    }

    /**
     * Sell an item from inventory, removing its effects and gaining gold.
     *
     * @param item Item to sell
     */
    public void sellItem(Item item) {
        if (inventory.remove(item)) {
            gold += item.getSellValue();
            item.removeFrom(this);
        }
    }

    /**
     * Add gold to the player's wallet.
     *
     * @param amount Amount of gold to add
     */
    public void addGold(int amount) {
        this.gold += amount;
    }

    // ==================== GETTERS AND SETTERS ====================

    public int getHealthPoints() {
        return healthPoints;
    }

    public int getMaxHealthPoints() {
        return maxHealthPoints;
    }

    public int getGold() {
        return gold;
    }

    /**
     * @return A defensive copy of the current hand
     */
    public List<Card> getCurrentHand() {
        return new ArrayList<>(currentHand);
    }

    public int getRemainingDiscards() {
        return maxDiscards - discardCount;
    }

    public Map<CardSuit, Integer> getSuitDamageBonus() {
        return suitDamageBonus;
    }

    public Map<HandType, Integer> getHandTypeDamageBonus() {
        return handTypeDamageBonus;
    }

    /**
     * @return A defensive copy of the inventory
     */
    public List<Item> getInventory() {
        return new ArrayList<>(inventory);
    }

    public Map<PointsCalculator.CardValueType, Integer> getCardValueTypeBonus() {
        return cardValueTypeBonus;
    }

    public Map<Integer, Integer> getCardCountBonus() {
        return cardCountBonus;
    }

    public int getRejectionBonus() {
        return rejectionBonus;
    }

    public void setRejectionBonus(int bonus) {
        this.rejectionBonus = bonus;
    }

    public int getBattleEndHealingAmount() {
        return battleEndHealingAmount;
    }

    public void setBattleEndHealingAmount(int amount) {
        this.battleEndHealingAmount = Math.max(0, amount);
    }

    public int getBaseDamageReduction() {
        return baseDamageReduction;
    }

    public void setBaseDamageReduction(int reduction) {
        this.baseDamageReduction = Math.max(0, reduction);
    }

    public int getLowHealthDamageReduction() {
        return lowHealthDamageReduction;
    }

    public void setLowHealthDamageReduction(int reduction) {
        this.lowHealthDamageReduction = Math.max(0, reduction);
    }

    public int getMaxDiscards() {
        return maxDiscards;
    }

    public void setMaxDiscards(int maxDiscards) {
        this.maxDiscards = maxDiscards;
    }
}