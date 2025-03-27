package main.java.fr.ynov.ortalab.domain.exceptions;

public class PlayerActionException extends Exception {
    public PlayerActionException(String message) {
        super(message);
    }

    public PlayerActionException(String message, Throwable cause) {
        super(message, cause);
    }
}
