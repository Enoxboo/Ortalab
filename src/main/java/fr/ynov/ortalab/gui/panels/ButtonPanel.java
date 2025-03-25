package main.java.fr.ynov.ortalab.gui.panels;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.game.GameManager;

import javax.swing.*;
import java.util.List;

public class ButtonPanel extends JPanel {
    private final GameManager gameManager;
    private final HandPanel handPanel;
    private final StatusPanel statusPanel;
    private final JButton playButton;
    private final JButton discardButton;

    public ButtonPanel(GameManager gameManager, HandPanel handPanel, StatusPanel statusPanel) {
        this.gameManager = gameManager;
        this.handPanel = handPanel;
        this.statusPanel = statusPanel;

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        playButton = createPlayButton();
        discardButton = createDiscardButton();

        add(playButton);
        add(discardButton);
    }

    // Getters for buttons
    public JButton getPlayCardsButton() {
        return playButton;
    }

    public JButton getDiscardButton() {
        return discardButton;
    }

    private JButton createPlayButton() {
        JButton button = new JButton("PLAY CARDS");
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
            showWarningMessage("Please select cards to play.");
            return;
        }

        try {
            // Use GameManager to process card play
            gameManager.selectHand(selectedCards);

            // Update status panel to reflect game state
            statusPanel.updateStatus(
                    gameManager.getPlayer(),
                    gameManager.getCurrentEnemy(),
                    gameManager.getCurrentLevel()
            );

            // Check game state after play
            checkGameState();
        } catch (Exception ex) {
            showWarningMessage("Error playing cards: " + ex.getMessage());
        }
    }

    private void handleDiscardAction() {
        List<Card> selectedCards = handPanel.getSelectedCards();

        if (selectedCards.isEmpty()) {
            showWarningMessage("Please select cards to discard.");
            return;
        }

        try {
            // Use GameManager to process discard
            gameManager.playerDiscard(selectedCards);

            // Update hand and status
            handPanel.removeCards(selectedCards);
            statusPanel.updateStatus(
                    gameManager.getPlayer(),
                    gameManager.getCurrentEnemy(),
                    gameManager.getCurrentLevel()
            );
        } catch (Exception ex) {
            showWarningMessage("Error discarding cards: " + ex.getMessage());
        }
    }

    private void checkGameState() {
        GameManager.GameState gameState = gameManager.getGameState();

        switch (gameState) {
            case GAME_OVER:
                showGameOverMessage();
                break;
            case VICTORY:
                showVictoryMessage();
                break;
        }
    }

    private void showWarningMessage(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Attention",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void showGameOverMessage() {
        JOptionPane.showMessageDialog(
                this,
                "Game Over! You were defeated.",
                "Game Ended",
                JOptionPane.INFORMATION_MESSAGE
        );
        System.exit(0);
    }

    private void showVictoryMessage() {
        JOptionPane.showMessageDialog(
                this,
                "Congratulations! You've won the game!",
                "Victory",
                JOptionPane.INFORMATION_MESSAGE
        );
        System.exit(0);
    }
}