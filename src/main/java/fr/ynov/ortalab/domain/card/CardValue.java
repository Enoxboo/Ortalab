package main.java.fr.ynov.ortalab.domain.card;

/**
 * Enumeration representing the possible values of a playing card.
 * Each value has a numeric value and a display symbol.
 * Values are ordered from TWO (2) to ACE (14).
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

    /**
     * Constructs a card value with the specified numeric value and symbol.
     *
     * @param numericValue the numeric value used for comparisons
     * @param symbol the display symbol for this card value
     */
    CardValue(int numericValue, String symbol) {
        this.numericValue = numericValue;
        this.symbol = symbol;
    }

    /**
     * Returns the numeric value of this card value.
     * Used for card comparisons and game logic.
     *
     * @return the numeric value (2-14)
     */
    public int getNumericValue() {
        return numericValue;
    }

    /**
     * Returns the symbol representing this card value.
     * Used in compact card representations.
     *
     * @return the symbol (2-10, J, Q, K, or A)
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Returns the string representation of this card value.
     *
     * @return the symbol representing this card value
     */
    @Override
    public String toString() {
        return symbol;
    }
}