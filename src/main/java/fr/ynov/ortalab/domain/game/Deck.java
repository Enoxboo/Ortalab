package main.java.fr.ynov.ortalab.domain.game;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.CardSuit;
import main.java.fr.ynov.ortalab.domain.CardValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Deck {
    private List<Card> originalCards;
    private List<Card> availableCards;
    private Set<Card> usedCards;

    /**
     * Create a standard 52-card deck
     */
    public Deck() {
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
    public void shuffle() {
        Collections.shuffle(availableCards);
    }

    /**
     * Draw a card from the available cards
     *
     * @return The drawn card
     * @throws IllegalStateException if no cards are available
     */
    public Card drawCard() {
        if (availableCards.isEmpty()) {
            throw new IllegalStateException("No cards left in the deck");
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

    /**
     * Get a list of unique cards to replace discarded or played cards
     *
     * @param count Number of unique cards to draw
     * @return List of unique cards
     */
    public List<Card> drawUniqueCards(int count) {
        List<Card> uniqueCards = new ArrayList<>();
        Set<Card> drawnCards = new HashSet<>();

        // Reset the deck if it's empty
        if (availableCards.isEmpty()) {
            reset();
        }

        while (uniqueCards.size() < count && !availableCards.isEmpty()) {
            Card card = drawCard();
            if (!drawnCards.contains(card)) {
                uniqueCards.add(card);
                drawnCards.add(card);
            }
        }

        // If still not enough unique cards, add non-unique cards
        while (uniqueCards.size() < count) {
            Card card = drawCard();
            uniqueCards.add(card);
        }

        return uniqueCards;
    }
}