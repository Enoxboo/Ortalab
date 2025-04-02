package main.java.fr.ynov.ortalab.main;

import main.java.fr.ynov.ortalab.domain.exceptions.DeckException;
import main.java.fr.ynov.ortalab.gui.OrtalabGUI;

public class ApplicationRunner {
    public static void main(String[] args) {
        try {
            OrtalabGUI.launch();
        } catch (DeckException e) {
            e.printStackTrace();
        }
    }
}