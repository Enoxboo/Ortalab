package main.java.fr.ynov.ortalab.domain.game.managers;

import main.java.fr.ynov.ortalab.config.GameConfig;
import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.exceptions.CardOperationException;
import main.java.fr.ynov.ortalab.domain.exceptions.DeckException;
import main.java.fr.ynov.ortalab.domain.exceptions.PlayerActionException;
import main.java.fr.ynov.ortalab.domain.game.Player;
import main.java.fr.ynov.ortalab.domain.game.Enemy;
import main.java.fr.ynov.ortalab.domain.game.Deck;

import java.util.List;

public class GameManager {
    private Player player;
    private Deck gameDeck;
    private EncounterManager encounterManager;
    private TurnManager turnManager;
    private GameState gameState;
    private static final int MAX_HAND_SIZE = GameConfig.MAX_HAND_SIZE;
    private static final int ACTIVE_HAND_SIZE = GameConfig.ACTIVE_HAND_SIZE;
    private static final int INITIAL_PLAYER_HP = GameConfig.INITIAL_PLAYER_HP;

    public enum GameState {
        INITIALIZING,
        DRAWING_CARDS,
        SELECTING_HAND,
        PLAYER_TURN,
        ENEMY_TURN,
        SHOP_VISIT,
        GAME_OVER,
        VICTORY
    }

    public GameManager() {
        initializeGame();
    }

    private void initializeGame() {
        player = new Player(INITIAL_PLAYER_HP);

        gameDeck = new Deck();

        encounterManager = new EncounterManager(player, gameDeck);
        turnManager = new TurnManager(player, encounterManager, gameDeck);

        gameState = GameState.INITIALIZING;
    }

    public void startGame() throws DeckException {
        encounterManager.startFirstEncounter();
        gameState = GameState.SELECTING_HAND;
    }

    public void continueFromShop() throws DeckException {
        encounterManager.startEncounter();
        player.resetDiscards();
        gameState = GameState.SELECTING_HAND;
    }

    public void selectHand(List<Card> selectedCards) throws DeckException {
        player.getCurrentHand().removeAll(selectedCards);

        turnManager.processPlayerTurn(selectedCards);

        refillHand();

        updateGameState();
    }

    private void refillHand() throws DeckException {
        int cardsToDraw = MAX_HAND_SIZE - player.getCurrentHand().size();

        if (cardsToDraw > 0) {
            List<Card> newCards = gameDeck.drawUniqueCards(cardsToDraw);
            player.getCurrentHand().addAll(newCards);
        }
    }

    public void playerDiscard(List<Card> cardsToDiscard) throws CardOperationException, PlayerActionException {
        if (gameState != GameState.SELECTING_HAND) {
            throw new IllegalStateException("Cannot discard cards at this time");
        }

        player.discard(cardsToDiscard, gameDeck);
    }

    private void updateGameState() {
        gameState = turnManager.getCurrentGameState();
    }

    public GameState getGameState() {
        return gameState;
    }

    public Player getPlayer() {
        return player;
    }

    public int getCurrentLevel() {
        return encounterManager.getCurrentLevel();
    }

    public Enemy getCurrentEnemy() {
        return encounterManager.getCurrentEnemy();
    }

    public Deck getDeck() {
        return gameDeck;
    }
}