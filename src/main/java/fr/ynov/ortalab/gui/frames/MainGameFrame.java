package main.java.fr.ynov.ortalab.gui.frames;

import main.java.fr.ynov.ortalab.domain.exceptions.DeckException;
import main.java.fr.ynov.ortalab.domain.game.managers.GameManager;
import main.java.fr.ynov.ortalab.domain.game.Enemy;
import main.java.fr.ynov.ortalab.domain.game.Player;
import main.java.fr.ynov.ortalab.gui.panels.*;
import main.java.fr.ynov.ortalab.domain.game.managers.GameManager.GameState;
import main.java.fr.ynov.ortalab.gui.components.CircleItemSlot;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.Box;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;

/**
 * The main game interface showing player and enemy status, player's hand,
 * and game controls. This frame serves as the central hub for gameplay.
 */
public class MainGameFrame extends JFrame {
    private final GameManager gameManager;

    // UI Components
    private HandPanel handPanel;
    private ButtonPanel buttonPanel;
    private StatusPanel playerStatusPanel;
    private StatusPanel enemyStatusPanel;
    private SortButtonPanel sortButtonPanel;
    private JLabel currentHandPointsLabel;

    // Inventory Components
    private static final int MAX_INVENTORY_SLOTS = 6;
    private final List<CircleItemSlot> inventorySlots = new ArrayList<>();
    private JPanel inventoryPanel;

    // Timer to check game state periodically
    private Timer gameStateTimer;

    /**
     * Constructs the main game frame with all necessary components.
     *
     * @param gameManager The game manager handling game logic
     * @throws DeckException If there's an issue initializing the player's deck
     */
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
        setResizable(false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Initialize game components and layout
        initializeGameComponents();

        // Create main content panel
        JPanel mainContentPanel = new JPanel(new BorderLayout());

        // Create center panel with status panels
        JPanel centerPanel = createStatusPanel();
        mainContentPanel.add(centerPanel, BorderLayout.CENTER);

        // Create and add inventory panel to the right
        inventoryPanel = createInventoryPanel();
        mainContentPanel.add(inventoryPanel, BorderLayout.EAST);

        // Add main content panel to the frame
        add(mainContentPanel, BorderLayout.CENTER);

        // Create bottom panel for game elements
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);

        // Create timer to check game state periodically
        createGameStateTimer();
    }

    /**
     * Factory method to create and show the main game frame.
     *
     * @param gameManager The game manager handling game logic
     */
    public static void launch(GameManager gameManager) {
        SwingUtilities.invokeLater(() -> {
            MainGameFrame frame;
            try {
                frame = new MainGameFrame(gameManager);
            } catch (DeckException e) {
                throw new RuntimeException(e);
            }
            frame.setVisible(true);
        });
    }

    //-------------------- Initialization Methods --------------------//

    /**
     * Initializes all game components including status panels,
     * hand panel, and game controls.
     *
     * @throws DeckException If there's an issue with the player's deck
     */
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

        // Add tutorial button
        JButton tutorialButton = createTutorialButton();
        sortButtonPanel.add(Box.createVerticalStrut(10));
        sortButtonPanel.add(tutorialButton);
    }

    /**
     * Creates the status panel containing player and enemy information.
     *
     * @return A JPanel containing player and enemy status
     */
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

    /**
     * Creates the inventory panel displaying player's current items.
     * Uses the same style as the ItemShopFrame's inventory panel.
     *
     * @return A JPanel displaying the player's inventory
     */
    private JPanel createInventoryPanel() {
        JPanel inventoryPanel = new JPanel();
        inventoryPanel.setPreferredSize(new Dimension(200, 0));
        inventoryPanel.setBorder(BorderFactory.createTitledBorder("Inventory"));
        inventoryPanel.setLayout(new BoxLayout(inventoryPanel, BoxLayout.Y_AXIS));

        JPanel slotsPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        slotsPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        for (int i = 0; i < MAX_INVENTORY_SLOTS; i++) {
            CircleItemSlot slot = new CircleItemSlot();
            inventorySlots.add(slot);
            slotsPanel.add(slot);
        }

        inventoryPanel.add(Box.createVerticalStrut(20));
        inventoryPanel.add(slotsPanel);
        inventoryPanel.add(Box.createVerticalGlue());

        // Initialize the inventory slots with player's current items
        updateInventoryDisplay();

        return inventoryPanel;
    }

    /**
     * Creates the bottom panel containing the player's hand and action buttons.
     *
     * @return A JPanel for the bottom section of the game
     */
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

    /**
     * Creates a tutorial button that opens the tutorial frame.
     *
     * @return A JButton that launches the tutorial
     */
    private JButton createTutorialButton() {
        JButton tutorialButton = new JButton("Tutorial");
        tutorialButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        tutorialButton.addActionListener(e -> TutorialFrame.showTutorial());
        return tutorialButton;
    }

    /**
     * Creates a timer to periodically check the game state
     * and respond to state changes (e.g., shop visits).
     */
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

    //-------------------- State Management Methods --------------------//

    /**
     * Shows the item shop and hides the main game frame temporarily.
     */
    private void showShop() {
        // Hide the game frame while showing the shop
        setVisible(false);

        // Show the item shop
        ItemShopFrame.displayShopMidGame(gameManager, this);
    }

    /**
     * Updates the inventory display to reflect player's current items.
     */
    private void updateInventoryDisplay() {
        Player player = gameManager.getPlayer();
        List<main.java.fr.ynov.ortalab.domain.game.Item> inventory = player.getInventory();

        // Clear all slots first
        for (CircleItemSlot slot : inventorySlots) {
            slot.clearItem();
        }

        // Populate slots with inventory items
        for (int i = 0; i < inventory.size(); i++) {
            if (i < inventorySlots.size()) {
                inventorySlots.get(i).setItem(inventory.get(i));
            }
        }
    }

    /**
     * Refreshes the game display after returning from the shop
     * or when game state changes.
     */
    public void refreshGameDisplay() {
        Player player = gameManager.getPlayer();
        Enemy enemy = gameManager.getCurrentEnemy();
        int currentLevel = gameManager.getCurrentLevel();

        playerStatusPanel.updateStatus(player, enemy, currentLevel);
        enemyStatusPanel.updateStatus(player, enemy, currentLevel);
        handPanel.sortHand(handPanel.getCurrentSortType());

        // Update inventory display with latest items
        updateInventoryDisplay();
    }

    /**
     * Cleans up resources when the frame is disposed.
     */
    @Override
    public void dispose() {
        if (gameStateTimer != null) {
            gameStateTimer.stop();
        }
        super.dispose();
    }
}