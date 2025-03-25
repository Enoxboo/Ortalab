package main.java.fr.ynov.ortalab.domain.game;

import java.util.function.Consumer;

public class Enemy {
    // Basic enemy attributes
    private int healthPoints;
    private int maxHealthPoints;
    private int attackDamage;
    private int attackCooldown;
    private int currentCooldown;

    // Enemy passive effect
    private EnemyPassive passive;

    /**
     * Constructor for a standard enemy
     *
     * @param hp Total health points
     * @param damage Base attack damage
     * @param cooldown Turns between attacks
     * @param passive Special passive ability
     */
    public Enemy(int hp, int damage, int cooldown, EnemyPassive passive) {
        this.healthPoints = hp;
        this.maxHealthPoints = hp;
        this.attackDamage = damage;
        this.attackCooldown = cooldown;
        this.currentCooldown = cooldown;
        this.passive = passive;
    }

    /**
     * Take damage from player's attack
     *
     * @param damage Amount of damage to take
     * @param selectedHand Player's selected hand for potential passive effects
     * @return Remaining health points
     */
    public int takeDamage(int damage, Object selectedHand) {
        // Apply passive effects if they modify incoming damage
        if (passive != null) {
            damage = passive.modifyIncomingDamage(damage, selectedHand);
        }

        this.healthPoints = Math.max(0, this.healthPoints - damage);
        return this.healthPoints;
    }

    /**
     * Determine if enemy can attack this turn
     *
     * @return true if enemy can attack, false otherwise
     */
    public boolean canAttack() {
        return currentCooldown <= 0;
    }

    /**
     * Execute enemy attack
     *
     * @param playerDamageReceiver Function to apply damage to player
     */
    public void attack(Consumer<Integer> playerDamageReceiver) {
        if (canAttack()) {
            playerDamageReceiver.accept(attackDamage);
            resetCooldown();
        }
    }

    /**
     * Reduce attack cooldown
     */
    public void reduceCooldown() {
        if (currentCooldown > 0) {
            currentCooldown--;
        }
    }

    /**
     * Reset attack cooldown to initial value
     */
    private void resetCooldown() {
        this.currentCooldown = this.attackCooldown;
    }

    // Getters
    public int getHealthPoints() {
        return healthPoints;
    }

    public int getMaxHealthPoints() {
        return maxHealthPoints;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public int getCurrentCooldown() {
        return currentCooldown;
    }

    /**
     * Interface for enemy passive abilities
     */
    public interface EnemyPassive {
        /**
         * Modify incoming damage based on specific conditions
         *
         * @param originalDamage Initial damage amount
         * @param context Additional context (e.g., player's hand)
         * @return Modified damage amount
         */
        int modifyIncomingDamage(int originalDamage, Object context);
    }

    // Example implementation of a passive
    public static class PairDamageReductionPassive implements EnemyPassive {
        @Override
        public int modifyIncomingDamage(int originalDamage, Object context) {
            // Reduce damage by 50% if player's hand contains a pair
            // This is a placeholder implementation - you'll need to add actual hand checking logic
            return originalDamage / 2;
        }
    }
}