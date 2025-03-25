package main.java.fr.ynov.ortalab.gui;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.game.GameManager;
import main.java.fr.ynov.ortalab.gui.frames.MainGameFrame;

import java.util.List;

public class OrtolabGUI {
    public static void main(String[] args) {
        // Create initial hand using GameManager
        List<Card> initialHand = GameManager.createSampleHand();

        // Launch the GUI
        MainGameFrame.launch(initialHand);
    }
}