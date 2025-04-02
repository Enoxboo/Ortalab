package main.java.fr.ynov.ortalab.domain.game;

/**
 * Represents the different types of poker hands, ordered from lowest to highest value.
 * Each hand type has a name and associated base point value for scoring.
 */
public enum HandType {
    HIGH_CARD("High Card", 10),
    PAIR("Pair", 20),
    TWO_PAIR("Two Pair", 40),
    THREE_OF_A_KIND("Three of a Kind", 80),
    STRAIGHT("Straight", 100),
    FLUSH("Flush", 125),
    FULL_HOUSE("Full House", 175),
    FOUR_OF_A_KIND("Four of a Kind", 400),
    STRAIGHT_FLUSH("Straight Flush", 600),
    ROYAL_FLUSH("Royal Flush", 2000);

    private final String name;
    private final int basePoints;

    /**
     * Creates a new hand type with the specified name and base point value.
     *
     * @param name The display name of the hand type
     * @param basePoints The base points awarded for this hand type
     */
    HandType(String name, int basePoints) {
        this.name = name;
        this.basePoints = basePoints;
    }

    /**
     * Gets the display name of this hand type.
     *
     * @return The hand type name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the base points awarded for this hand type.
     *
     * @return The base points
     */
    public int getBasePoints() {
        return basePoints;
    }

    @Override
    public String toString() {
        return name;
    }
}