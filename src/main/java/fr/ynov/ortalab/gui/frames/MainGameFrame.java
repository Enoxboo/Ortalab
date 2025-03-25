package main.java.fr.ynov.ortalab.gui.frames;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.game.GameManager;
import main.java.fr.ynov.ortalab.domain.game.Enemy;
import main.java.fr.ynov.ortalab.domain.game.Player;
import main.java.fr.ynov.ortalab.gui.panels.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainGameFrame extends JFrame {
    private final GameManager gameManager;
    private HandPanel handPanel;
    private ButtonPanel buttonPanel;
    private StatusPanel statusPanel;
    private SortButtonPanel sortButtonPanel;

    public MainGameFrame(GameManager gameManager) {
        this.gameManager = gameManager;

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

        // Initialize game components
        initializeGameComponents();

        // Create bottom panel to contain game elements
        JPanel bottomPanel = createBottomPanel();

        // Add bottom panel to the main frame
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void initializeGameComponents() {
        Player player = gameManager.getPlayer();
        Enemy enemy = gameManager.getCurrentEnemy();

        // Create status panel to show game state
        statusPanel = new StatusPanel(player, enemy, gameManager.getCurrentLevel());

        // Create hand panel with initial hand
        handPanel = new HandPanel(player.getCurrentHand());

        // Create sort button panel
        sortButtonPanel = new SortButtonPanel(handPanel);

        // Create button panel with game actions
        buttonPanel = new ButtonPanel(gameManager, handPanel, statusPanel);
    }

    private JPanel createBottomPanel() {
        // Create bottom panel to contain game elements
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Create a panel to hold hand panel with sorting buttons
        JPanel handContainer = new JPanel(new BorderLayout());
        handContainer.add(sortButtonPanel, BorderLayout.WEST);

        // Modify HandPanel layout to wrap cards
        handPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        handContainer.add(new JScrollPane(handPanel), BorderLayout.CENTER);

        // Add hand container to top of bottom panel
        bottomPanel.add(handContainer, BorderLayout.NORTH);

        // Create a panel for buttons and status
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // Add game buttons
        JButton discardButton = buttonPanel.getDiscardButton();
        JButton playButton = buttonPanel.getPlayCardsButton();
        actionPanel.add(discardButton);
        actionPanel.add(new JScrollPane(statusPanel));
        actionPanel.add(playButton);

        // Add action panel to bottom of bottom panel
        bottomPanel.add(actionPanel, BorderLayout.SOUTH);

        return bottomPanel;
    }

    public static void launch(GameManager gameManager) {
        SwingUtilities.invokeLater(() -> {
            MainGameFrame frame = new MainGameFrame(gameManager);
            frame.setVisible(true);
        });
    }
}