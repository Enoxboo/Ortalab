package main.java.fr.ynov.ortalab.domain.exceptions;

/**
 * Exception for deck-related operations that fail
 */
public class DeckException extends GameException {
    public DeckException(String message) {
        super(message);
    }

    public DeckException(String message, Throwable cause) {
        super(message, cause);
    }
}