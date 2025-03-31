package main.java.fr.ynov.ortalab.gui.frames;

import main.java.fr.ynov.ortalab.domain.exceptions.DeckException;
import main.java.fr.ynov.ortalab.domain.game.managers.GameManager;
import main.java.fr.ynov.ortalab.domain.game.Enemy;
import main.java.fr.ynov.ortalab.domain.game.Player;
import main.java.fr.ynov.ortalab.gui.panels.*;
import main.java.fr.ynov.ortalab.domain.PointsCalculator;
import main.java.fr.ynov.ortalab.domain.game.managers.GameManager.GameState;

import javax.swing.*;
import java.awt.*;

public class MainGameFrame extends JFrame {
    private final GameManager gameManager;
    private HandPanel handPanel;
    private ButtonPanel buttonPanel;
    private StatusPanel playerStatusPanel;
    private StatusPanel enemyStatusPanel;
    private SortButtonPanel sortButtonPanel;
    private JLabel currentHandPointsLabel;

    // Timer to check game state periodically
    private Timer gameStateTimer;

    public MainGameFrame(GameManager gameManager) throws DeckException {
        this.gameManager = gameManager;

        // Set fixed size and prevent resizing
        int frameWidth = 1200;
        int frameHeight = 675;

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

        // Create main content panel
        JPanel mainContentPanel = new JPanel(new BorderLayout());

        // Create center panel with status panels
        JPanel centerPanel = createStatusPanel();
        mainContentPanel.add(centerPanel, BorderLayout.CENTER);

        // Add main content panel to the frame
        add(mainContentPanel, BorderLayout.CENTER);

        // Create bottom panel for game elements
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);

        // Create timer to check game state periodically
        createGameStateTimer();
    }

    private void createGameStateTimer() {
        gameStateTimer = new Timer(100, e -> {
            if (gameManager.getGameState() == GameState.SHOP_VISIT) {
                // If we need to visit the shop, stop the timer, show the shop, and hide this frame
                gameStateTimer.stop();
                showShop();
            }
        });
        gameStateTimer.start();
    }

    private void showShop() {
        // Hide the game frame while showing the shop
        setVisible(false);

        // Show the item shop
        ItemShopFrame.displayShopMidGame(gameManager, this);
    }

    private void initializeGameComponents() throws DeckException {
        Player player = gameManager.getPlayer();
        Enemy enemy = gameManager.getCurrentEnemy();

        // Create separate status panels for player and enemy
        playerStatusPanel = new StatusPanel(player, enemy, gameManager.getCurrentLevel(), true);
        enemyStatusPanel = new StatusPanel(player, enemy, gameManager.getCurrentLevel(), false);

        // Create hand panel with initial hand
        handPanel = new HandPanel(player.getCurrentHand(), gameManager);

        // Create current hand points label
        currentHandPointsLabel = new JLabel("Damages : 0", SwingConstants.CENTER);
        currentHandPointsLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Create sort button panel WITH gameManager
        sortButtonPanel = new SortButtonPanel(handPanel, gameManager);

        // Create button panel with game actions
        buttonPanel = new ButtonPanel(
                gameManager,
                handPanel,
                playerStatusPanel,
                enemyStatusPanel,
                currentHandPointsLabel
        );
        JButton tutorialButton = createTutorialButton();
        sortButtonPanel.add(Box.createVerticalStrut(10));
        sortButtonPanel.add(tutorialButton);
    }

    private JButton createTutorialButton() {
        JButton tutorialButton = new JButton("Tutoriel");
        tutorialButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        tutorialButton.addActionListener(e -> TutorialFrame.showTutorial());
        return tutorialButton;
    }

    private JPanel createStatusPanel() {
        // Create a panel to split status panels
        JPanel statusContainer = new JPanel(new GridLayout(1, 2, 10, 0));
        statusContainer.add(playerStatusPanel);
        statusContainer.add(enemyStatusPanel);

        // Make status panels larger and non-scrollable
        playerStatusPanel.setRows(15);
        enemyStatusPanel.setRows(15);

        return statusContainer;
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

        // Create a panel for buttons and damages
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // Add game buttons and damages preview
        JButton discardButton = buttonPanel.getDiscardButton();
        JButton playButton = buttonPanel.getPlayCardsButton();
        actionPanel.add(discardButton);
        actionPanel.add(currentHandPointsLabel);
        actionPanel.add(playButton);

        // Combine hand container and action panel
        bottomPanel.add(handContainer, BorderLayout.CENTER);
        bottomPanel.add(actionPanel, BorderLayout.SOUTH);

        return bottomPanel;
    }

    public static void launch(GameManager gameManager) {
        SwingUtilities.invokeLater(() -> {
            MainGameFrame frame = null;
            try {
                frame = new MainGameFrame(gameManager);
            } catch (DeckException e) {
                throw new RuntimeException(e);
            }
            frame.setVisible(true);
        });
    }

    public void refreshGameDisplay() {
        Player player = gameManager.getPlayer();
        Enemy enemy = gameManager.getCurrentEnemy();
        int currentLevel = gameManager.getCurrentLevel();

        playerStatusPanel.updateStatus(player, enemy, currentLevel);
        enemyStatusPanel.updateStatus(player, enemy, currentLevel);
        handPanel.sortHand(handPanel.getCurrentSortType());
    }
    // Clean up resources when closing
    @Override
    public void dispose() {
        if (gameStateTimer != null) {
            gameStateTimer.stop();
        }
        super.dispose();
    }
}