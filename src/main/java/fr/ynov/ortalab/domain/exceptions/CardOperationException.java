package main.java.fr.ynov.ortalab.domain.exceptions;

/**
 * Exception for card-related operations that fail
 */
public class CardOperationException extends GameException {
    public CardOperationException(String message) {
        super(message);
    }

    public CardOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}