package main.java.fr.ynov.ortalab.domain.game;

import main.java.fr.ynov.ortalab.domain.card.Card;
import main.java.fr.ynov.ortalab.domain.card.CardSuit;
import main.java.fr.ynov.ortalab.domain.card.CardValue;
import main.java.fr.ynov.ortalab.domain.exceptions.DeckException;

import java.util.*;

public class Deck {
    private List<Card> originalCards;
    private List<Card> availableCards;
    private Set<Card> usedCards;

    public Deck() {
        try {
            initializeDeck();
        } catch (Exception e) {
            // Log the error
            throw new RuntimeException("Failed to initialize deck", e);
        }
    }

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
     * Shuffle the available cards
     */
    private void shuffle() {
        Collections.shuffle(availableCards);
    }

    /**
     * Draw a card from the available cards
     *
     * @return The drawn card
     * @throws IllegalStateException if no cards are available
     */
    public Card drawCard() throws DeckException {
        if (availableCards.isEmpty()) {
            throw new DeckException("No cards left in the deck. Cannot draw.");
        }
        Card drawnCard = availableCards.remove(availableCards.size() - 1);
        usedCards.add(drawnCard);
        return drawnCard;
    }

    /**
     * Add a card back to the available cards (if not already used)
     *
     * @param card The card to be returned to the deck
     */
    public void returnCard(Card card) {
        if (!usedCards.contains(card) && !availableCards.contains(card)) {
            availableCards.add(card);
        }
    }

    /**
     * Get the number of cards remaining in the deck
     *
     * @return Remaining card count
     */
    public int getRemainingCards() {
        return availableCards.size();
    }

    /**
     * Check if the deck is empty
     *
     * @return true if no cards remain, false otherwise
     */
    public boolean isEmpty() {
        return availableCards.isEmpty();
    }

    /**
     * Reset and reshuffle the deck, clearing used cards
     */
    public void reset() {
        availableCards = new ArrayList<>(originalCards);
        usedCards = new HashSet<>();
        shuffle();
    }

    public void replenishIfEmpty() {
        if (availableCards.isEmpty()) {
            reset();
        }
    }

    /**
     * Get a list of unique cards to replace discarded or played cards
     *
     * @param count Number of unique cards to draw
     * @return List of unique cards
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

        if (uniqueCards.size() < count) {
            reset();
            while (uniqueCards.size() < count && !availableCards.isEmpty()) {
                Card drawnCard = drawCard();
                uniqueCards.add(drawnCard);
            }
        }

        return new ArrayList<>(uniqueCards);
    }

    public Set<Card> getUsedCards() {
        return new HashSet<>(usedCards);
    }
}