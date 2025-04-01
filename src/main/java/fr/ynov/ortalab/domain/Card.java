package main.java.fr.ynov.ortalab.domain;

public record Card(CardValue value, CardSuit suit) {

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

}