package main.java.fr.ynov.ortalab.domain.game;

import main.java.fr.ynov.ortalab.domain.card.Card;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents an enemy in the card-based combat game.
 * Handles enemy health, attacks, and cooldown mechanics.
 */
public class Enemy {
    private int healthPoints;
    private final int attackDamage;
    private final int attackCooldown;
    private int currentCooldown;

    /**
     * Creates a new enemy with specified health, attack damage, and cooldown.
     * Cooldown represents turns between enemy attacks.
     *
     * @param healthPoints Initial enemy health
     * @param attackDamage Damage dealt per attack
     * @param attackCooldown Turns between attacks
     */
    public Enemy(int healthPoints, int attackDamage, int attackCooldown) {
        this.healthPoints = healthPoints;
        this.attackDamage = attackDamage;
        this.attackCooldown = attackCooldown;
        this.currentCooldown = attackCooldown;
    }

    /**
     * Apply damage to the enemy based on player's attack.
     * The cards parameter allows for potential card-specific interactions.
     *
     * @param damage Amount of damage to apply
     * @param cards Cards used for the attack, allowing for special effects
     * @return Remaining health points
     */
    public int takeDamage(int damage, List<Card> cards) {
        this.healthPoints -= damage;

        // Ensure health doesn't go below 0
        if (this.healthPoints < 0) {
            this.healthPoints = 0;
        }

        return this.healthPoints;
    }

    /**
     * Execute an attack against the player using a damage handler.
     * Resets the cooldown after attacking.
     *
     * @param damageHandler Consumer that processes the damage value
     */
    public void attack(Consumer<Integer> damageHandler) {
        damageHandler.accept(attackDamage);
        currentCooldown = attackCooldown;
    }

    /**
     * Reduce the attack cooldown by one turn.
     * Called at the end of each combat turn.
     */
    public void reduceCooldown() {
        if (currentCooldown > 0) {
            currentCooldown--;
        }
    }

    /**
     * Check if the enemy can attack this turn.
     *
     * @return true if cooldown is zero, false otherwise
     */
    public boolean canAttack() {
        return currentCooldown == 0;
    }

    /**
     * @return Current enemy health points
     */
    public int getHealthPoints() {
        return healthPoints;
    }

    /**
     * @return Base attack damage
     */
    public int getAttackDamage() {
        return attackDamage;
    }

    /**
     * @return Current turns remaining until next attack
     */
    public int getCurrentCooldown() {
        return currentCooldown;
    }
}