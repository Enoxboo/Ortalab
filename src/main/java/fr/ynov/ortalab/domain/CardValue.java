package main.java.fr.ynov.ortalab.domain;

/**
 * Enumeration representing the possible values of a card.
 */
public enum CardValue {
    TWO(2, "2"),
    THREE(3, "3"),
    FOUR(4, "4"),
    FIVE(5, "5"),
    SIX(6, "6"),
    SEVEN(7, "7"),
    EIGHT(8, "8"),
    NINE(9, "9"),
    TEN(10, "10"),
    JACK(11, "J"),
    QUEEN(12, "Q"),
    KING(13, "K"),
    ACE(14, "A");

    private final int numericValue;
    private final String symbol;

    CardValue(int numericValue, String symbol) {
        this.numericValue = numericValue;
        this.symbol = symbol;
    }

    public int getNumericValue() {
        return numericValue;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}