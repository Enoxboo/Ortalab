package main.java.fr.ynov.ortalab.domain.game;

import main.java.fr.ynov.ortalab.domain.card.Card;
import java.util.List;
import java.util.function.Consumer;

public class Enemy {
    private int healthPoints;
    private int attackDamage;
    private int attackCooldown;
    private int currentCooldown;

    public Enemy(int healthPoints, int attackDamage, int attackCooldown) {
        this.healthPoints = healthPoints;
        this.attackDamage = attackDamage;
        this.attackCooldown = attackCooldown;
        this.currentCooldown = attackCooldown;
    }

    public int takeDamage(int damage, List<Card> cards) {
        this.healthPoints -= damage;

        // Ensure health doesn't go below 0
        if (this.healthPoints < 0) {
            this.healthPoints = 0;
        }

        return this.healthPoints;
    }

    public void attack(Consumer<Integer> damageHandler) {
        damageHandler.accept(attackDamage);
        currentCooldown = attackCooldown;
    }

    public void reduceCooldown() {
        if (currentCooldown > 0) {
            currentCooldown--;
        }
    }

    public boolean canAttack() {
        return currentCooldown == 0;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public int getCurrentCooldown() {
        return currentCooldown;
    }
}