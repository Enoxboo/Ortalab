package main.java.fr.ynov.ortalab.domain.game;

import java.util.function.Consumer;

public class Enemy {
    // Basic enemy attributes
    private int healthPoints;
    private int maxHealthPoints;
    private int attackDamage;
    private int attackCooldown;
    private int currentCooldown;



    /**
     * Constructor for a standard enemy
     *
     * @param hp Total health points
     * @param damage Base attack damage
     * @param cooldown Turns between attacks
     */
    public Enemy(int hp, int damage, int cooldown) {
        this.healthPoints = hp;
        this.maxHealthPoints = hp;
        this.attackDamage = damage;
        this.attackCooldown = cooldown;
        this.currentCooldown = cooldown;
    }

    /**
     * Take damage from player's attack
     *
     * @param damage Amount of damage to take
     * @param selectedHand Player's selected hand for potential passive effects
     * @return Remaining health points
     */
    public int takeDamage(int damage, Object selectedHand) {
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

}