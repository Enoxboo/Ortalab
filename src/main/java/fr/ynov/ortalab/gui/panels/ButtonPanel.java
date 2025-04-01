package main.java.fr.ynov.ortalab.gui.panels;

import main.java.fr.ynov.ortalab.domain.card.Card;
import main.java.fr.ynov.ortalab.domain.HandEvaluator;
import main.java.fr.ynov.ortalab.domain.game.managers.GameManager;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import java.util.List;

public class ButtonPanel extends JPanel {
    private final GameManager gameManager;

    private final HandPanel handPanel;
    private final StatusPanel playerStatusPanel;
    private final StatusPanel enemyStatusPanel;
    private final JLabel currentHandPointsLabel;
    private final JButton playButton;
    private final JButton discardButton;

    public ButtonPanel(GameManager gameManager, HandPanel handPanel, StatusPanel playerStatusPanel, StatusPanel enemyStatusPanel, JLabel currentHandPointsLabel) {
        this.gameManager = gameManager;
        this.handPanel = handPanel;
        this.playerStatusPanel = playerStatusPanel;
        this.enemyStatusPanel = enemyStatusPanel;
        this.currentHandPointsLabel = currentHandPointsLabel;

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        playButton = createPlayButton();
        discardButton = createDiscardButton();

        // Add listener to update hand points when card selection changes
        handPanel.addCardSelectionListener(this::updateHandPoints);

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

    private void updateHandPoints() {
        List<Card> selectedCards = handPanel.getSelectedCards();

        if (selectedCards.isEmpty()) {
            currentHandPointsLabel.setText("Hand: None");
            return;
        }

        // Create a HandEvaluator to get the hand type and base points
        HandEvaluator evaluator = new HandEvaluator(selectedCards);
        String handType = evaluator.getHandType();
        int basePoints = evaluator.getPoints();

        // Display only the hand type and base points, not the total damage
        currentHandPointsLabel.setText(String.format("%s: %d", handType, basePoints));
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

            // Remove the played cards from the hand panel
            handPanel.removeCards(selectedCards);

            // Update both status panels to reflect game state
            playerStatusPanel.updateStatus(
                    gameManager.getPlayer(),
                    gameManager.getCurrentEnemy(),
                    gameManager.getCurrentLevel()
            );
            enemyStatusPanel.updateStatus(
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

            // Update hand and both status panels
            handPanel.removeCards(selectedCards);
            playerStatusPanel.updateStatus(
                    gameManager.getPlayer(),
                    gameManager.getCurrentEnemy(),
                    gameManager.getCurrentLevel()
            );
            enemyStatusPanel.updateStatus(
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