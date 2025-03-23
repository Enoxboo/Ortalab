package main.java.fr.ynov.ortalab.domain;

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

    HandType(String name, int basePoints) {
        this.name = name;
        this.basePoints = basePoints;
    }

    public String getName() {
        return name;
    }

    public int getBasePoints() {
        return basePoints;
    }

    @Override
    public String toString() {
        return name;
    }
}