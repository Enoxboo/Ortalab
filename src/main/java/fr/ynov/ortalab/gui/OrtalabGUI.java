package main.java.fr.ynov.ortalab.gui;

import main.java.fr.ynov.ortalab.domain.exceptions.DeckException;
import main.java.fr.ynov.ortalab.domain.game.managers.GameManager;
import main.java.fr.ynov.ortalab.gui.frames.MainGameFrame;

import javax.swing.JOptionPane;

public class OrtalabGUI {
    public static void main(String[] args) throws DeckException {
        try {
            GameManager gameManager = new GameManager();

            gameManager.startGame();
            MainGameFrame.launch(gameManager);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error starting game: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}