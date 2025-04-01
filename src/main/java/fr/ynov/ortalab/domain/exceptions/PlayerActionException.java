package main.java.fr.ynov.ortalab.domain.exceptions;

/**
 * Exception for player action operations that fail
 */
public class PlayerActionException extends GameException {
    public PlayerActionException(String message) {
        super(message);
    }

    public PlayerActionException(String message, Throwable cause) {
        super(message, cause);
    }
}