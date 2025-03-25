package main.java.fr.ynov.ortalab.gui;

import main.java.fr.ynov.ortalab.domain.game.GameManager;
import main.java.fr.ynov.ortalab.gui.frames.MainGameFrame;

public class OrtalabGUI {
    public static void main(String[] args) {
        // Create game manager
        GameManager gameManager = new GameManager();

        // Start first encounter
        gameManager.startEncounter();

        // Launch the GUI with the game manager
        MainGameFrame.launch(gameManager);
    }
}