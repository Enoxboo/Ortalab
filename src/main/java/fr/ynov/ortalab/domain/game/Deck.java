package main.java.fr.ynov.ortalab.domain.game;

import main.java.fr.ynov.ortalab.domain.card.Card;
import main.java.fr.ynov.ortalab.domain.card.CardSuit;
import main.java.fr.ynov.ortalab.domain.card.CardValue;
import main.java.fr.ynov.ortalab.domain.exceptions.DeckException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 * Represents a deck of playing cards with standard operations.
 * Manages card drawing, tracking, and reshuffling.
 */
public class Deck {
    private List<Card> originalCards;  // The complete set of cards in the standard deck
    private List<Card> availableCards; // Cards currently available for drawing
    private Set<Card> usedCards;       // Cards that have been drawn and not returned

    /**
     * Creates a new deck with all standard cards and shuffles them.
     * Handles initialization failures with a RuntimeException.
     */
    public Deck() {
        try {
            initializeDeck();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize deck", e);
        }
    }

    /**
     * Initializes the deck with a standard set of cards (all suits and values).
     * Resets the deck to its initial state after initialization.
     */
    private void initializeDeck() {
        originalCards = new ArrayList<>();
        for (CardSuit suit : CardSuit.values()) {
            for (CardValue value : CardValue.values()) {
                originalCards.add(new Card(value, suit));
            }
        }
        reset();
    }

    /**
     * Checks if the deck is empty and replenishes if necessary.
     * Resets and reshuffles the deck when no cards are available.
     */
    public void replenishIfEmpty() {
        if (availableCards.isEmpty()) {
            reset();
        }
    }

    /**
     * Reset and reshuffle the deck, clearing used cards and starting fresh.
     * All cards return to the available cards pool.
     */
    public void reset() {
        availableCards = new ArrayList<>(originalCards);
        usedCards = new HashSet<>();
        shuffle();
    }

    /**
     * Shuffles the available cards for randomized drawing.
     */
    private void shuffle() {
        Collections.shuffle(availableCards);
    }

    /**
     * Draw a card from the available cards pool.
     * Tracks drawn cards in the used cards set.
     *
     * @return The drawn card
     * @throws DeckException if no cards are available
     */
    public Card drawCard() throws DeckException {
        if (availableCards.isEmpty()) {
            throw new DeckException("No cards left in the deck. Cannot draw.");
        }
        Card drawnCard = availableCards.removeLast();
        usedCards.add(drawnCard);
        return drawnCard;
    }

    /**
     * Get a list of unique cards to replace discarded or played cards.
     * If unique cards aren't available, reset the deck and try again.
     *
     * @param count Number of unique cards to draw
     * @return List of unique cards
     * @throws DeckException if requested more cards than exist in the deck
     */
    public List<Card> drawUniqueCards(int count) throws DeckException {
        if (count > originalCards.size()) {
            throw new DeckException("Requested more unique cards than available in the deck");
        }

        if (availableCards.isEmpty()) {
            reset();
        }

        Set<Card> uniqueCards = new LinkedHashSet<>();

        while (uniqueCards.size() < count && !availableCards.isEmpty()) {
            Card drawnCard = drawCard();
            uniqueCards.add(drawnCard);
        }

        // If we didn't get enough unique cards, reset and try again
        if (uniqueCards.size() < count) {
            reset();
            while (uniqueCards.size() < count && !availableCards.isEmpty()) {
                Card drawnCard = drawCard();
                uniqueCards.add(drawnCard);
            }
        }

        return new ArrayList<>(uniqueCards);
    }

    /**
     * Add a card back to the available cards if it's not already in use or available.
     *
     * @param card The card to be returned to the deck
     */
    public void returnCard(Card card) {
        if (!usedCards.contains(card) && !availableCards.contains(card)) {
            availableCards.add(card);
        }
    }

    /**
     * Check if the deck is empty.
     *
     * @return true if no cards remain, false otherwise
     */
    public boolean isEmpty() {
        return availableCards.isEmpty();
    }

    /**
     * @return A defensive copy of the used cards set
     */
    public Set<Card> getUsedCards() {
        return new HashSet<>(usedCards);
    }
}