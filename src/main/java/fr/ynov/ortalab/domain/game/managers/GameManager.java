package main.java.fr.ynov.ortalab.domain.game.managers;

import main.java.fr.ynov.ortalab.domain.Card;
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
    private static final int MAX_HAND_SIZE = 8;
    private static final int ACTIVE_HAND_SIZE = 5;
    private static final int INITIAL_PLAYER_HP = 100;

    public enum GameState {
        INITIALIZING,
        DRAWING_CARDS,
        SELECTING_HAND,
        PLAYER_TURN,
        ENEMY_TURN,
        GAME_OVER,
        VICTORY
    }

    public GameManager() {
        initializeGame();
    }

    private void initializeGame() {
        // Create player with initial health
        player = new Player(INITIAL_PLAYER_HP);

        // Create new deck
        gameDeck = new Deck();

        // Initialize managers
        encounterManager = new EncounterManager(player, gameDeck);
        turnManager = new TurnManager(player, encounterManager, gameDeck);

        // Set initial game state
        gameState = GameState.INITIALIZING;
    }

    public void startGame() {
        encounterManager.startFirstEncounter();
        gameState = GameState.SELECTING_HAND;
    }

    public void selectHand(List<Card> selectedCards) {
        // Discard played cards from the current hand
        player.getCurrentHand().removeAll(selectedCards);

        // Process the turn with selected cards
        turnManager.processPlayerTurn(selectedCards);

        // Refill hand with new unique cards if needed
        refillHand();

        updateGameState();
    }

    private void refillHand() {
        // Calculate how many cards need to be drawn to reach MAX_HAND_SIZE
        int cardsToDraw = MAX_HAND_SIZE - player.getCurrentHand().size();

        if (cardsToDraw > 0) {
            // Draw unique cards to fill the hand
            List<Card> newCards = gameDeck.drawUniqueCards(cardsToDraw);
            player.getCurrentHand().addAll(newCards);
        }
    }

    public void playerDiscard(List<Card> cardsToDiscard) {
        if (gameState != GameState.SELECTING_HAND) {
            throw new IllegalStateException("Cannot discard cards at this time");
        }

        player.discard(cardsToDiscard, gameDeck);
    }

    private void updateGameState() {
        gameState = turnManager.getCurrentGameState();
    }

    // Getters
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