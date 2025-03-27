package main.java.fr.ynov.ortalab.gui.panels;

import main.java.fr.ynov.ortalab.domain.game.managers.GameManager;
import main.java.fr.ynov.ortalab.gui.frames.DeckViewFrame;

import javax.swing.*;
import java.awt.*;

public class SortButtonPanel extends JPanel {
    private final HandPanel handPanel;
    private final GameManager gameManager;
    private final JButton sortByValueButton;
    private final JButton sortBySuitButton;
    private final JButton deckViewButton;
    private DeckViewFrame deckViewFrame;

    public SortButtonPanel(HandPanel handPanel, GameManager gameManager) {
        this.handPanel = handPanel;
        this.gameManager = gameManager;

        // Set layout to vertical
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Create buttons
        sortByValueButton = createSortByValueButton();
        sortBySuitButton = createSortBySuitButton();
        deckViewButton = createDeckViewButton();

        // Add some vertical spacing between buttons
        add(Box.createVerticalStrut(20));
        add(sortByValueButton);
        add(Box.createVerticalStrut(10));
        add(sortBySuitButton);
        add(Box.createVerticalStrut(10));
        add(deckViewButton);

        // Adjust panel width
        setPreferredSize(new Dimension(120, getPreferredSize().height));
    }

    private JButton createSortByValueButton() {
        JButton button = new JButton("Valeur");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(e -> {
            handPanel.sortHand(HandPanel.SortType.VALUE);
            updateSortButtonAppearance(true);
        });
        return button;
    }

    private JButton createSortBySuitButton() {
        JButton button = new JButton("Couleur");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(e -> {
            handPanel.sortHand(HandPanel.SortType.SUIT);
            updateSortButtonAppearance(false);
        });
        return button;
    }

    private void updateSortButtonAppearance(boolean isValueSort) {
        sortByValueButton.setBackground(isValueSort ? Color.LIGHT_GRAY : null);
        sortBySuitButton.setBackground(isValueSort ? null : Color.LIGHT_GRAY);
    }

    private JButton createDeckViewButton() {
        JButton button = new JButton("Deck");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(e -> {
            if (deckViewFrame == null || !deckViewFrame.isVisible()) {
                deckViewFrame = new DeckViewFrame(gameManager.getDeck());
                deckViewFrame.setVisible(true);
            }

            // Always update the deck view when opened or brought to front
            deckViewFrame.updateDeckView();
            deckViewFrame.toFront();
        });
        return button;
    }
}