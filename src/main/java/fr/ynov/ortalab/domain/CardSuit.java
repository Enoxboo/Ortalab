package main.java.fr.ynov.ortalab.domain;

/**
 * Enumeration representing the four suits of a standard deck of cards.
 */
public enum CardSuit {
    CLUBS("Clubs"),
    HEARTS("Hearts"),
    DIAMONDS("Diamonds"),
    SPADES("Spades");

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