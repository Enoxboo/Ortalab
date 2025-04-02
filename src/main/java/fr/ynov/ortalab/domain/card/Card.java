package main.java.fr.ynov.ortalab.domain.card;

/**
 * Represents a playing card with a value and suit.
 * Implemented as a record for immutability and automatic implementation of core methods.
 *
 * @param value the card's value (e.g., ACE, KING, QUEEN)
 * @param suit the card's suit (e.g., HEARTS, SPADES)
 */
public record Card(CardValue value, CardSuit suit) {

    /**
     * Returns a string representation of the card in the format "value of suit".
     * @return full text representation of the card
     */
    @Override
    public String toString() {
        return value + " of " + suit;
    }

    /**
     * Returns a compact string representation of the card with suit symbol.
     * @return card value symbol followed by suit Unicode symbol
     */
    public String toShortString() {
        String suitSymbol = switch (suit) {
            case CLUBS -> "♣";
            case HEARTS -> "♥";
            case DIAMONDS -> "♦";
            case SPADES -> "♠";
        };
        return value.getSymbol() + suitSymbol;
    }

    /**
     * Compares this card with another object for equality.
     * Cards are equal if they have the same value and suit.
     *
     * @param obj the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Card card = (Card) obj;
        return value == card.value && suit == card.suit;
    }
}