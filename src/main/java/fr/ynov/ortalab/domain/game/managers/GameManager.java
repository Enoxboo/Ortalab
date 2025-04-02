package main.java.fr.ynov.ortalab.domain.game.managers;

import main.java.fr.ynov.ortalab.config.GameConfig;
import main.java.fr.ynov.ortalab.domain.card.Card;
import main.java.fr.ynov.ortalab.domain.exceptions.CardOperationException;
import main.java.fr.ynov.ortalab.domain.exceptions.DeckException;
import main.java.fr.ynov.ortalab.domain.exceptions.PlayerActionException;
import main.java.fr.ynov.ortalab.domain.game.Player;
import main.java.fr.ynov.ortalab.domain.game.Enemy;
import main.java.fr.ynov.ortalab.domain.game.Deck;

import java.util.List;

/**
 * Main controller for the game that orchestrates all game components.
 * Manages game state transitions and coordinates between player, encounters, and turns.
 */
public class GameManager {
    private Player player;
    private Deck gameDeck;
    private EncounterManager encounterManager;
    private TurnManager turnManager;
    private GameState gameState;

    private static final int MAX_HAND_SIZE = GameConfig.MAX_HAND_SIZE;
    private static final int INITIAL_PLAYER_HP = GameConfig.INITIAL_PLAYER_HP;

    /**
     * Represents the possible states of the game.
     */
    public enum GameState {
        INITIALIZING,
        SELECTING_HAND,
        SHOP_VISIT,
        GAME_OVER,
        VICTORY
    }

    /**
     * Creates a new game manager and initializes game components.
     */
    public GameManager() {
        initializeGame();
    }

    // --- Game lifecycle methods ---

    /**
     * Initializes all game components and sets initial state.
     */
    private void initializeGame() {
        player = new Player(INITIAL_PLAYER_HP);
        gameDeck = new Deck();
        encounterManager = new EncounterManager(player, gameDeck);
        turnManager = new TurnManager(player, encounterManager, gameDeck);
        gameState = GameState.INITIALIZING;
    }

    /**
     * Starts the game by initiating the first encounter.
     * @throws DeckException if there's an issue with deck operations
     */
    public void startGame() throws DeckException {
        encounterManager.startFirstEncounter();
        gameState = GameState.SELECTING_HAND;
    }

    /**
     * Continues the game after a shop visit by starting a new encounter.
     * @throws DeckException if there's an issue with deck operations
     */
    public void continueFromShop() throws DeckException {
        encounterManager.startEncounter();
        player.resetDiscards();
        gameState = GameState.SELECTING_HAND;
    }

    // --- Turn management methods ---

    /**
     * Processes the player's selected cards, handles the turn, and updates the game state.
     *
     * @param selectedCards cards the player chose to play
     * @throws DeckException if there's an issue with deck operations
     */
    public void selectHand(List<Card> selectedCards) throws DeckException {
        player.getCurrentHand().removeAll(selectedCards);

        turnManager.processPlayerTurn(selectedCards);

        refillHand();

        updateGameState();
    }

    /**
     * Allows the player to discard cards if in the appropriate game state.
     *
     * @param cardsToDiscard cards to be discarded
     * @throws CardOperationException if there's an issue with the card operation
     * @throws PlayerActionException if the action is invalid
     */
    public void playerDiscard(List<Card> cardsToDiscard) throws CardOperationException, PlayerActionException {
        if (gameState != GameState.SELECTING_HAND) {
            throw new IllegalStateException("Cannot discard cards at this time");
        }

        player.discard(cardsToDiscard, gameDeck);
    }

    // --- Helper methods ---

    /**
     * Refills the player's hand to the maximum size.
     * @throws DeckException if there's an issue with deck operations
     */
    private void refillHand() throws DeckException {
        int cardsToDraw = MAX_HAND_SIZE - player.getCurrentHand().size();

        if (cardsToDraw > 0) {
            List<Card> newCards = gameDeck.drawUniqueCards(cardsToDraw);
            player.getCurrentHand().addAll(newCards);
        }
    }

    /**
     * Updates the game state based on the turn manager's state.
     */
    private void updateGameState() {
        gameState = turnManager.getCurrentGameState();
    }

    // --- Getters ---

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