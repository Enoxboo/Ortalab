package main.java.fr.ynov.ortalab.gui;

import main.java.fr.ynov.ortalab.domain.exceptions.DeckException;
import main.java.fr.ynov.ortalab.domain.game.managers.GameManager;
import main.java.fr.ynov.ortalab.gui.frames.MainGameFrame;

public class OrtalabGUI {
    public static void main(String[] args) throws DeckException {
        GameManager gameManager = new GameManager();
        gameManager.startGame();
        MainGameFrame.launch(gameManager);
    }
}