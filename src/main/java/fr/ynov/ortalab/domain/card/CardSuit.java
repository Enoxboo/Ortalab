package main.java.fr.ynov.ortalab.domain.card;

/**
 * Enumeration representing the four suits of a standard deck of cards.
 */
public enum CardSuit {
    CLUBS("Clubs"),
    HEARTS("Hearts"),
    SPADES("Spades"),
    DIAMONDS("Diamonds");

    private final String name;

    CardSuit(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}