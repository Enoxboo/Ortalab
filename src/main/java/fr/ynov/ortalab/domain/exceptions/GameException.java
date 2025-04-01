package main.java.fr.ynov.ortalab.domain.exceptions;

/**
 * Base exception for all game-related errors
 */
public class GameException extends Exception {
    public GameException(String message) {
        super(message);
    }

    public GameException(String message, Throwable cause) {
        super(message, cause);
    }
}