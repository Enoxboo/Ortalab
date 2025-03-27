package main.java.fr.ynov.ortalab.domain.exceptions;

public class InvalidGameStateException extends GameException {
  public InvalidGameStateException(String message) {
    super(message);
  }
}