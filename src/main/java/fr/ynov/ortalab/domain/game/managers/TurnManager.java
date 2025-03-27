package main.java.fr.ynov.ortalab.domain.game.managers;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.game.Player;
import main.java.fr.ynov.ortalab.domain.game.Enemy;
import main.java.fr.ynov.ortalab.domain.game.Deck;
import main.java.fr.ynov.ortalab.domain.game.managers.GameManager.GameState;

import java.util.List;

public class TurnManager {
    private Player player;
    private EncounterManager encounterManager;
    private GameState currentGameState;
    private Deck gameDeck;

    public TurnManager(Player player, EncounterManager encounterManager, Deck gameDeck) {
        this.player = player;
        this.encounterManager = encounterManager;
        this.gameDeck = gameDeck;  // Initialize the game deck
        this.currentGameState = GameState.INITIALIZING;
    }

    public void processPlayerTurn(List<Card> selectedCards) {
        // Select hand and calculate damage
        player.selectHand(selectedCards);
        int playerDamage = player.calculateHandDamage();

        // Get current enemy
        Enemy currentEnemy = encounterManager.getCurrentEnemy();

        // Apply damage to enemy
        int remainingEnemyHP = currentEnemy.takeDamage(playerDamage, selectedCards);

        // Discard the used cards
        discardUsedCards(selectedCards);

        // Check if enemy is defeated
        if (remainingEnemyHP <= 0) {
            encounterManager.completeEncounter(true);

            // Check if game is complete
            if (encounterManager.isGameComplete()) {
                currentGameState = GameState.VICTORY;
                return;
            }

            // Start next encounter
            encounterManager.startEncounter();
            player.resetDiscards();
            currentGameState = GameState.SELECTING_HAND;
            return;
        }

        // Process enemy turn
        processEnemyTurn(currentEnemy);
    }

    /**
     * Discard the cards used in the attack
     *
     * @param usedCards List of cards to be discarded
     */
    private void discardUsedCards(List<Card> usedCards) {
        // Return used cards to the deck as used cards
        for (Card card : usedCards) {
            gameDeck.returnCard(card);
        }
    }

    private void processEnemyTurn(Enemy currentEnemy) {
        // Reduce enemy cooldown
        currentEnemy.reduceCooldown();

        // Enemy attacks if ready
        if (currentEnemy.canAttack()) {
            currentEnemy.attack(damage -> {
                int remainingPlayerHP = player.takeDamage(damage);

                if (remainingPlayerHP <= 0) {
                    currentGameState = GameState.GAME_OVER;
                }
            });
        }


        // If not game over, return to player turn
        if (currentGameState != GameState.GAME_OVER) {
            currentGameState = GameState.SELECTING_HAND;
        }
    }

    // Getter for current game state
    public GameState getCurrentGameState() {
        return currentGameState;
    }
}