package main.java.fr.ynov.ortalab.gui.frames;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.gui.panels.HandPanel;
import main.java.fr.ynov.ortalab.gui.panels.ButtonPanel;
import main.java.fr.ynov.ortalab.gui.panels.ScorePanel;
import main.java.fr.ynov.ortalab.gui.panels.SortButtonPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainGameFrame extends JFrame {
    private HandPanel handPanel;
    private ButtonPanel buttonPanel;
    private ScorePanel scorePanel;
    private SortButtonPanel sortButtonPanel;

    public MainGameFrame(List<Card> initialHand) {
        // Set fixed size and prevent resizing
        int frameWidth = 1200;
        int frameHeight = 700;

        setTitle("Ortolab");
        setSize(frameWidth, frameHeight);
        setMinimumSize(new Dimension(frameWidth, frameHeight));
        setMaximumSize(new Dimension(frameWidth, frameHeight));
        setPreferredSize(new Dimension(frameWidth, frameHeight));

        // Prevent resizing
        setResizable(false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Center the frame on the screen
        setLocationRelativeTo(null);

        // Create panels
        handPanel = new HandPanel(initialHand);
        scorePanel = new ScorePanel();
        buttonPanel = new ButtonPanel(handPanel, scorePanel);

        // Create a new panel for sorting buttons on the left
        sortButtonPanel = new SortButtonPanel(handPanel);

        // Create bottom panel to contain both hand and action elements
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Create a panel to hold hand panel with sorting buttons
        JPanel handContainer = new JPanel(new BorderLayout());
        handContainer.add(sortButtonPanel, BorderLayout.WEST);

        // Modify HandPanel layout to wrap cards
        handPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Added spacing
        handContainer.add(new JScrollPane(handPanel), BorderLayout.CENTER);

        // Add hand container to top of bottom panel
        bottomPanel.add(handContainer, BorderLayout.NORTH);

        // Create a panel for buttons and score
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // Add discard button
        JButton discardButton = buttonPanel.getDiscardButton();
        actionPanel.add(discardButton);

        // Add score panel
        actionPanel.add(new JScrollPane(scorePanel));

        // Add play button
        JButton playButton = buttonPanel.getPlayCardsButton();
        actionPanel.add(playButton);

        // Add action panel to bottom of bottom panel
        bottomPanel.add(actionPanel, BorderLayout.SOUTH);

        // Add bottom panel to bottom of the main frame
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public static void launch(List<Card> initialHand) {
        SwingUtilities.invokeLater(() -> {
            MainGameFrame frame = new MainGameFrame(initialHand);
            frame.setVisible(true);
        });
    }
}