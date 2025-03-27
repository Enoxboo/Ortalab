package main.java.fr.ynov.ortalab.domain.game;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.PointsCalculator;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private int healthPoints;
    private int maxHealthPoints;
    private int gold;
    private int discardCount;
    private int totalDiscardCount;
    private final int MAX_DISCARDS = 3;
    private final int MAX_HAND_SIZE = 8;
    private final int ACTIVE_HAND_SIZE = 5;
    private final int MAX_DISCARDS_PER_ENEMY = 3;

    // Inventory and modifiers
    private List<Item> inventory;
    private int criticalChance;
    private List<Card> currentHand;
    private List<Card> selectedHand;

    /**
     * Constructor for a new Player
     *
     * @param initialHP Initial health points
     */
    public Player(int initialHP) {
        this.healthPoints = initialHP;
        this.maxHealthPoints = initialHP;
        this.gold = 0;
        this.discardCount = 0;
        this.totalDiscardCount = 0;
        this.criticalChance = 5; // Base 5% critical chance
        this.inventory = new ArrayList<>();
        this.currentHand = new ArrayList<>();
        this.selectedHand = new ArrayList<>();
    }

    /**
     * Take damage from an enemy attack
     *
     * @param damage Amount of damage to take
     * @return Remaining health points
     */
    public int takeDamage(int damage) {
        this.healthPoints = Math.max(0, this.healthPoints - damage);
        return this.healthPoints;
    }

    /**
     * Heal the player
     *
     * @param amount Amount of healing
     */
    public void heal(int amount) {
        this.healthPoints = Math.min(this.maxHealthPoints, this.healthPoints + amount);
    }

    /**
     * Calculate damage based on selected poker hand
     *
     * @return Calculated damage
     */
    public int calculateHandDamage() {
        return PointsCalculator.calculateScore(selectedHand);
    }

    /**
     * Draw cards to fill the current hand
     *
     * @param deck The deck to draw cards from
     */
    public void drawCards(Deck deck) {
        currentHand.clear();
        for (int i = 0; i < MAX_HAND_SIZE; i++) {
            currentHand.add(deck.drawCard());
        }
    }

    /**
     * Select cards for active hand
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
     * Discard selected cards and draw replacements
     *
     * @param cardsToDiscard Cards to be discarded
     * @param deck Deck to draw new cards from
     * @throws IllegalStateException if discard limit is reached
     */
    public void discard(List<Card> cardsToDiscard, Deck deck) {
        if (discardCount >= MAX_DISCARDS_PER_ENEMY) {
            throw new IllegalStateException("Maximum discards for this enemy reached");
        }

        // Remove discarded cards from current hand
        currentHand.removeAll(cardsToDiscard);

        // Draw replacement cards
        for (int i = 0; i < cardsToDiscard.size(); i++) {
            currentHand.add(deck.drawCard());
        }

        discardCount++;
        totalDiscardCount++; // Increment total discard count
    }

    public void resetDiscards() {
        this.discardCount = 0;
    }

    /**
     * Add an item to the player's inventory
     *
     * @param item Item to add
     * @return true if item was added successfully
     */
    public boolean addItem(Item item) {
        if (inventory.size() < 6) {
            inventory.add(item);
            applyItemEffect(item);
            return true;
        }
        return false;
    }


    /**
     * Sell an item from inventory
     *
     * @param item Item to sell
     * @return true if item was sold successfully
     */
    public boolean sellItem(Item item) {
        if (inventory.remove(item)) {
            gold += item.getSellValue();
            removeItemEffect(item);
            return true;
        }
        return false;
    }

    /**
     * Apply the effects of an item
     *
     * @param item Item to apply
     */
    private void applyItemEffect(Item item) {
        // Implement specific item effects here
        // For example:
        // if (item.getType() == ItemType.CRITICAL_CHANCE) {
        //     criticalChance += item.getValue();
        // }
    }

    /**
     * Remove the effects of an item
     *
     * @param item Item to remove effects for
     */
    private void removeItemEffect(Item item) {
        // Implement removing item effects here
    }

    public void addGold(int amount) {
        if (amount > 0) {
            this.gold += amount;
        }
    }

    // Getters and Setters
    public int getHealthPoints() {
        return healthPoints;
    }

    public int getMaxHealthPoints() {
        return maxHealthPoints;
    }

    public int getGold() {
        return gold;
    }

    public int getCriticalChance() {
        return criticalChance;
    }

    public List<Card> getCurrentHand() {
        return new ArrayList<>(currentHand);
    }

    public List<Card> getSelectedHand() {
        return new ArrayList<>(selectedHand);
    }

    public int getRemainingDiscards() {
        return MAX_DISCARDS_PER_ENEMY - discardCount;
    }

    public int getTotalDiscardCount() {
        return totalDiscardCount;
    }

    public int getDiscardCount() {
        return discardCount;
    }

    public void setDiscardCount(int count) {
        this.discardCount = count;
    }
}