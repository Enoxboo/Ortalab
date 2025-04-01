package main.java.fr.ynov.ortalab.domain.game;

import main.java.fr.ynov.ortalab.config.GameConfig;
import main.java.fr.ynov.ortalab.domain.card.Card;
import main.java.fr.ynov.ortalab.domain.card.CardSuit;
import main.java.fr.ynov.ortalab.domain.HandType;
import main.java.fr.ynov.ortalab.domain.PointsCalculator;
import main.java.fr.ynov.ortalab.domain.exceptions.DeckException;
import main.java.fr.ynov.ortalab.domain.exceptions.PlayerActionException;
import main.java.fr.ynov.ortalab.domain.exceptions.CardOperationException;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Player {
    private int healthPoints;
    private final int maxHealthPoints;
    private int gold;
    private int discardCount;
    private final int MAX_HAND_SIZE = GameConfig.MAX_HAND_SIZE;
    private final int ACTIVE_HAND_SIZE = GameConfig.ACTIVE_HAND_SIZE;
    private final int MAX_DISCARDS_PER_ENEMY = GameConfig.MAX_DISCARDS_PER_ENEMY;

    private final List<Item> inventory;
    private final List<Card> currentHand;
    private List<Card> selectedHand;
    private final Map<CardSuit, Integer> suitDamageBonus;
    private final Map<HandType, Integer> handTypeDamageBonus;

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
        return PointsCalculator.calculateScore(selectedHand, this);
    }

    /**
     * Draw cards to fill the current hand
     *
     * @param deck The deck to draw cards from
     */
    public void drawCards(Deck deck) throws DeckException {
        currentHand.clear();

        while (currentHand.size() < MAX_HAND_SIZE) {
            deck.replenishIfEmpty();
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

    private void validateDiscardAction(List<Card> cardsToDiscard) throws PlayerActionException {
        if (discardCount >= MAX_DISCARDS_PER_ENEMY) {
            throw new PlayerActionException("Maximum discards for this enemy reached");
        }

        if (cardsToDiscard == null || cardsToDiscard.isEmpty()) {
            throw new PlayerActionException("No cards selected for discard");
        }

        if (cardsToDiscard.size() > ACTIVE_HAND_SIZE) {
            throw new PlayerActionException("Must discard between 1 and 5 cards");
        }
    }

    public void resetDiscards() {
        this.discardCount = 0;
    }

    /**
     * Add an item to the player's inventory
     *
     * @param item Item to add
     */
    public void addItem(Item item) {
        if (inventory.size() < GameConfig.MAX_INVENTORY_SIZE) {
            inventory.add(item);
            item.applyTo(this); // Use the item's built-in effect
        }
    }

    /**
     * Sell an item from inventory
     *
     * @param item Item to sell
     */
    public void sellItem(Item item) {
        if (inventory.remove(item)) {
            gold += item.getSellValue();
            item.removeFrom(this);
        }
    }

    public void addGold(int amount) {
        this.gold += amount;
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

    public List<Card> getCurrentHand() {
        return new ArrayList<>(currentHand);
    }

    public int getRemainingDiscards() {
        return MAX_DISCARDS_PER_ENEMY - discardCount;
    }

    public Map<CardSuit, Integer> getSuitDamageBonus() {
        return new EnumMap<>(suitDamageBonus);
    }

    public Map<HandType, Integer> getHandTypeDamageBonus() {
        return new EnumMap<>(handTypeDamageBonus);
    }

    public List<Item> getInventory() {
        return new ArrayList<>(inventory);
    }
}