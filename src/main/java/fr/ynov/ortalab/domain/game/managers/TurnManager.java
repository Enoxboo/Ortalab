package main.java.fr.ynov.ortalab.domain.game.managers;

import main.java.fr.ynov.ortalab.domain.card.Card;
import main.java.fr.ynov.ortalab.domain.exceptions.DeckException;
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
        this.gameDeck = gameDeck;
        this.currentGameState = GameState.INITIALIZING;
    }

    public void processPlayerTurn(List<Card> selectedCards) throws DeckException {
        player.selectHand(selectedCards);
        int playerDamage = player.calculateHandDamage();

        Enemy currentEnemy = encounterManager.getCurrentEnemy();

        int remainingEnemyHP = currentEnemy.takeDamage(playerDamage, selectedCards);

        discardUsedCards(selectedCards);

        // If enemy HP is 0 or less, handle enemy defeat
        if (remainingEnemyHP <= 0) {
            handleEnemyDefeated();
            return;
        }

        // If enemy is still alive, process enemy turn
        processEnemyTurn(currentEnemy);
    }

    private void handleEnemyDefeated() throws DeckException {
        // Check if we should visit the shop after this encounter
        boolean shouldVisitShop = encounterManager.shouldVisitShop();

        // Complete the encounter, which updates the level
        encounterManager.completeEncounter(true);

        // Check if the game is complete
        if (encounterManager.isGameComplete()) {
            currentGameState = GameState.VICTORY;
            return;
        }

        // If we should visit the shop after this encounter
        if (shouldVisitShop) {
            currentGameState = GameState.SHOP_VISIT;
            return;
        }

        // Otherwise start the next encounter
        encounterManager.startEncounter();
        player.resetDiscards();
        currentGameState = GameState.SELECTING_HAND;
    }

    private void discardUsedCards(List<Card> usedCards) {
        for (Card card : usedCards) {
            gameDeck.returnCard(card);
        }
    }

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

    public GameState getCurrentGameState() {
        return currentGameState;
    }
}