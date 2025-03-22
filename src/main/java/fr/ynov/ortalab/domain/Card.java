package main.java.fr.ynov.ortalab.domain;

public class Card {
    private final CardValue value;
    private final CardSuit suit;

    public Card(CardValue value, CardSuit suit) {
        this.value = value;
        this.suit = suit;
    }

    // Getters and setters
    public CardValue getValue() {
        return value;
    }

    public CardSuit getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return value + " of " + suit;
    }

    public String toShortString() {
        String suitSymbol = switch (suit) {
            case CLUBS -> "♣";
            case HEARTS -> "♥";
            case DIAMONDS -> "♦";
            case SPADES -> "♠";
            default -> "?";
        };
        return value.getSymbol() + suitSymbol;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Card card = (Card) obj;
        return value == card.value && suit == card.suit;
    }

    @Override
    public int hashCode() {
        return 31 * value.hashCode() + suit.hashCode();
    }
}