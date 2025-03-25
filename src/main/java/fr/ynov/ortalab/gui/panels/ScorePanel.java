package main.java.fr.ynov.ortalab.gui.panels;

import javax.swing.*;

public class ScorePanel extends JTextArea {
    public ScorePanel() {
        setEditable(false);
        setRows(5);
        setColumns(40);
    }

    public void displayScore(String scoreBreakdown) {
        setText(scoreBreakdown);
    }
}