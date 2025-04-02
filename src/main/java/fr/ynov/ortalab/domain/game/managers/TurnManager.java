package main.java.fr.ynov.ortalab.domain.game.managers;

import main.java.fr.ynov.ortalab.domain.card.Card;
import main.java.fr.ynov.ortalab.domain.exceptions.DeckException;
import main.java.fr.ynov.ortalab.domain.game.Player;
import main.java.fr.ynov.ortalab.domain.game.Enemy;
import main.java.fr.ynov.ortalab.domain.game.Deck;
import main.java.fr.ynov.ortalab.domain.game.managers.GameManager.GameState;

import java.util.List;

/**
 * Manages the turn sequence between the player and enemies.
 * Handles damage calculation, state transitions, and game progression.
 */
public class TurnManager {
    private final Player player;
    private final EncounterManager encounterManager;
    private final Deck gameDeck;
    private GameState currentGameState;

    /**
     * Creates a new turn manager with the specified components.
     */
    public TurnManager(Player player, EncounterManager encounterManager, Deck gameDeck) {
        this.player = player;
        this.encounterManager = encounterManager;
        this.gameDeck = gameDeck;
        this.currentGameState = GameState.INITIALIZING;
    }

    /**
     * Processes the player's turn with the selected cards.
     * Handles damage calculation, enemy health reduction, and state transitions.
     *
     * @param selectedCards cards the player chose to play this turn
     * @throws DeckException if there's an issue with deck operations
     */
    public void processPlayerTurn(List<Card> selectedCards) throws DeckException {
        player.selectHand(selectedCards);
        int playerDamage = player.calculateHandDamage();

        Enemy currentEnemy = encounterManager.getCurrentEnemy();
        int remainingEnemyHP = currentEnemy.takeDamage(playerDamage, selectedCards);

        discardUsedCards(selectedCards);

        // Handle the outcome of the player's attack
        if (remainingEnemyHP <= 0) {
            handleEnemyDefeated();
        } else {
            processEnemyTurn(currentEnemy);
        }
    }

    /**
     * Returns the current game state.
     */
    public GameState getCurrentGameState() {
        return currentGameState;
    }

    /**
     * Handles what happens when an enemy is defeated.
     * Manages shop visits, level progression, and game completion.
     *
     * @throws DeckException if there's an issue with deck operations
     */
    private void handleEnemyDefeated() throws DeckException {
        boolean shouldVisitShop = encounterManager.shouldVisitShop();
        encounterManager.completeEncounter(true);

        if (encounterManager.isGameComplete()) {
            currentGameState = GameState.VICTORY;
            return;
        }

        if (shouldVisitShop) {
            currentGameState = GameState.SHOP_VISIT;
            return;
        }

        // Start next encounter
        encounterManager.startEncounter();
        player.resetDiscards();
        currentGameState = GameState.SELECTING_HAND;
    }

    /**
     * Processes the enemy's turn including attacks and cooldown reduction.
     * Updates game state based on battle outcome.
     *
     * @param currentEnemy the enemy taking its turn
     */
    private void processEnemyTurn(Enemy currentEnemy) {
        currentEnemy.reduceCooldown();

        if (currentEnemy.canAttack()) {
            currentEnemy.attack(damage -> {
                int remainingPlayerHP = player.takeDamage(damage);

                if (remainingPlayerHP <= 0) {
                    currentGameState = GameState.GAME_OVER;
                }
            });
        }

        if (currentGameState != GameState.GAME_OVER) {
            currentGameState = GameState.SELECTING_HAND;
        }
    }

    /**
     * Returns used cards to the deck.
     */
    private void discardUsedCards(List<Card> usedCards) {
        for (Card card : usedCards) {
            gameDeck.returnCard(card);
        }
    }
}