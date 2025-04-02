package main.java.fr.ynov.ortalab.domain.card;

/**
 * Enumeration representing the four suits of a standard deck of cards.
 * Each suit has a display name used in string representations.
 */
public enum CardSuit {
    CLUBS("Clubs"),
    HEARTS("Hearts"),
    SPADES("Spades"),
    DIAMONDS("Diamonds");

    private final String name;

    /**
     * Constructs a card suit with the specified display name.
     *
     * @param name the display name of the suit
     */
    CardSuit(String name) {
        this.name = name;
    }

    /**
     * Returns the display name of this suit.
     *
     * @return the suit's display name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the string representation of this suit.
     *
     * @return the suit's display name
     */
    @Override
    public String toString() {
        return name;
    }
}