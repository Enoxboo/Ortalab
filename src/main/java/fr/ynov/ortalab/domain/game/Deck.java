package main.java.fr.ynov.ortalab.domain.game;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.CardSuit;
import main.java.fr.ynov.ortalab.domain.CardValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;

    /**
     * Create a standard 52-card deck
     */
    public Deck() {
        cards = new ArrayList<>();
        for (CardSuit suit : CardSuit.values()) {
            for (CardValue value : CardValue.values()) {
                cards.add(new Card(value, suit));
            }
        }
        shuffle();
    }

    /**
     * Shuffle the deck
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Draw a card from the deck
     *
     * @return The drawn card
     * @throws IllegalStateException if deck is empty
     */
    public Card drawCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("No cards left in the deck");
        }
        return cards.remove(cards.size() - 1);
    }

    /**
     * Get the number of cards remaining in the deck
     *
     * @return Remaining card count
     */
    public int getRemainingCards() {
        return cards.size();
    }

    /**
     * Check if the deck is empty
     *
     * @return true if no cards remain, false otherwise
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Reset and reshuffle the deck
     */
    public void reset() {
        cards.clear();
        for (CardSuit suit : CardSuit.values()) {
            for (CardValue value : CardValue.values()) {
                cards.add(new Card(value, suit));
            }
        }
        shuffle();
    }
}