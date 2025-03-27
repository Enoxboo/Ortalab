package main.java.fr.ynov.ortalab.domain.game.managers;

import main.java.fr.ynov.ortalab.domain.Card;
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

        if (remainingEnemyHP <= 0) {
            encounterManager.completeEncounter(true);

            if (encounterManager.isGameComplete()) {
                currentGameState = GameState.VICTORY;
                return;
            }

            encounterManager.startEncounter();
            player.resetDiscards();
            currentGameState = GameState.SELECTING_HAND;
            return;
        }
        processEnemyTurn(currentEnemy);
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
