package main.java.fr.ynov.ortalab.gui.panels;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.CardSuit;
import main.java.fr.ynov.ortalab.domain.CardValue;
import main.java.fr.ynov.ortalab.game.GameManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ButtonPanel extends JPanel {
    private final HandPanel handPanel;
    private final ScorePanel scorePanel;
    private final JButton playButton;
    private final JButton discardButton;

    public ButtonPanel(HandPanel handPanel, ScorePanel scorePanel) {
        this.handPanel = handPanel;
        this.scorePanel = scorePanel;

        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        playButton = createPlayButton();
        discardButton = createDiscardButton();

        add(playButton);
        add(discardButton);
    }

    // Getter for Play Button
    public JButton getPlayCardsButton() {
        return playButton;
    }

    // Getter for Discard Button
    public JButton getDiscardButton() {
        return discardButton;
    }

    private JButton createPlayButton() {
        JButton button = new JButton("PLAY");
        button.addActionListener(e -> handlePlayAction());
        return button;
    }

    private JButton createDiscardButton() {
        JButton button = new JButton("DISCARD");
        button.addActionListener(e -> handleDiscardAction());
        return button;
    }

    private void handlePlayAction() {
        List<Card> selectedCards = handPanel.getSelectedCards();

        if (selectedCards.isEmpty()) {
            showWarningMessage("Veuillez sélectionner des cartes à jouer.");
            return;
        }

        int cardScore = GameManager.calculatePlayScore(selectedCards);
        updateScorePanel(cardScore);
        handPanel.removeCards(selectedCards);
    }

    private void handleDiscardAction() {
        List<Card> selectedCards = handPanel.getSelectedCards();

        if (selectedCards.isEmpty()) {
            showWarningMessage("Veuillez sélectionner des cartes à échanger.");
            return;
        }

        handPanel.removeCards(selectedCards);
        updateScorePanel(0);
    }

    private void showWarningMessage(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Attention",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void updateScorePanel(int roundScore) {
        scorePanel.displayScore(
                String.format("Round Score: +%d", roundScore)
        );
    }
}