package main.java.fr.ynov.ortalab.gui.frames;

import main.java.fr.ynov.ortalab.domain.game.Item;
import main.java.fr.ynov.ortalab.domain.game.ItemShop;
import main.java.fr.ynov.ortalab.domain.game.Player;
import main.java.fr.ynov.ortalab.domain.game.managers.GameManager;
import main.java.fr.ynov.ortalab.gui.components.CircleItemSlot;
import main.java.fr.ynov.ortalab.gui.components.ShopItemPanel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.Box;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the item shop interface where players can purchase items between battles.
 * The shop allows players to:
 * - Purchase available items using gold
 * - Reroll the shop offerings at an increasing cost
 * - Sell existing inventory items
 */
public class ItemShopFrame extends JFrame {
    // Shop configuration constants
    private static final int MAX_SHOP_ITEMS = 3;
    private static final int MAX_INVENTORY_SLOTS = 6;
    private static final int BASE_REROLL_COST = 1;

    // Core game references
    private final GameManager gameManager;
    private final Player player;
    private final ItemShop itemShop;
    private final JFrame parentFrame;

    // UI components
    private final List<ShopItemPanel> shopItemPanels = new ArrayList<>();
    private final List<CircleItemSlot> inventorySlots = new ArrayList<>();
    private JLabel goldLabel;
    private JButton rerollButton;

    // Gameplay state
    private int rerollCount = 0;

    /**
     * Private constructor to enforce usage of the factory method.
     *
     * @param gameManager The game manager instance
     * @param parentFrame The parent frame to return to after shopping
     */
    private ItemShopFrame(GameManager gameManager, JFrame parentFrame) {
        this.gameManager = gameManager;
        this.player = gameManager.getPlayer();
        this.itemShop = new ItemShop();
        this.parentFrame = parentFrame;

        setTitle("Item Shop");
        setSize(1200, 660);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Handle window closing event to properly return to the game
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                returnToGame();
            }
        });

        initializeComponents();
    }

    /**
     * Factory method to create and display the shop during gameplay.
     *
     * @param gameManager The game manager instance
     * @param parentFrame The parent frame to return to after shopping
     */
    public static void displayShopMidGame(GameManager gameManager, JFrame parentFrame) {
        ItemShopFrame shopFrame = new ItemShopFrame(gameManager, parentFrame);
        shopFrame.setVisible(true);
    }

    //-------------------- UI Initialization Methods --------------------//

    /**
     * Initializes all UI components and generates initial shop items.
     */
    private void initializeComponents() {
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel shopPanel = createShopPanel();
        mainPanel.add(shopPanel, BorderLayout.CENTER);

        JPanel inventoryPanel = createInventoryPanel();
        mainPanel.add(inventoryPanel, BorderLayout.EAST);

        JPanel bottomPanel = createBottomPanel();

        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Initialize shop state
        generateShopItems();
        updateInventoryDisplay();
        updateGoldDisplay();
        updateRerollButton();
    }

    /**
     * Creates the shop panel displaying available items for purchase.
     *
     * @return A JPanel containing the shop items
     */
    private JPanel createShopPanel() {
        JPanel shopPanel = new JPanel(new BorderLayout());
        shopPanel.setBorder(BorderFactory.createTitledBorder("Choose an Item"));

        JPanel itemsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));

        // Create item panels with purchase functionality
        for (int i = 0; i < MAX_SHOP_ITEMS; i++) {
            ShopItemPanel itemPanel = new ShopItemPanel();
            itemPanel.setPreferredSize(new Dimension(200, 250));

            itemPanel.addBuyButtonListener(e -> handleItemPurchase(itemPanel));

            shopItemPanels.add(itemPanel);
            itemsPanel.add(itemPanel);
        }

        // Add reroll button in a panel above the items
        JPanel rerollPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rerollButton = new JButton("Reroll Shop (1 gold)");
        rerollButton.addActionListener(e -> handleShopReroll());
        rerollPanel.add(rerollButton);

        shopPanel.add(rerollPanel, BorderLayout.NORTH);
        shopPanel.add(itemsPanel, BorderLayout.CENTER);
        return shopPanel;
    }

    /**
     * Creates the inventory panel showing player's current items.
     * Items can be sold via right-click.
     *
     * @return A JPanel displaying the player's inventory
     */
    private JPanel createInventoryPanel() {
        JPanel inventoryPanel = new JPanel();
        inventoryPanel.setPreferredSize(new Dimension(250, 0));
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

        return inventoryPanel;
    }

    /**
     * Creates the bottom panel with gold display and exit button.
     *
     * @return A JPanel for the bottom of the frame
     */
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        goldLabel = new JLabel("Gold: 0");
        goldLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton exitButton = new JButton("Continue");
        exitButton.addActionListener(e -> returnToGame());

        bottomPanel.add(goldLabel, BorderLayout.WEST);
        bottomPanel.add(exitButton, BorderLayout.EAST);

        return bottomPanel;
    }

    //-------------------- Shop Functionality Methods --------------------//

    /**
     * Generates random items to display in the shop.
     */
    private void generateShopItems() {
        List<Item> randomItems = itemShop.getRandomItems(MAX_SHOP_ITEMS);

        for (int i = 0; i < Math.min(randomItems.size(), shopItemPanels.size()); i++) {
            shopItemPanels.get(i).setItem(randomItems.get(i));
        }
    }

    /**
     * Handles the purchase of an item from the shop.
     * Checks if the player has enough gold and inventory space.
     *
     * @param itemPanel The panel containing the item to purchase
     */
    private void handleItemPurchase(ShopItemPanel itemPanel) {
        Item item = itemPanel.getItem();

        if (item == null) {
            return;
        }

        // Check if player has enough gold
        if (player.getGold() < item.getBuyValue()) {
            JOptionPane.showMessageDialog(this,
                    "Not enough gold to purchase this item!",
                    "Purchase Failed",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check if inventory has space
        if (player.getInventory().size() >= MAX_INVENTORY_SLOTS) {
            JOptionPane.showMessageDialog(this,
                    "Inventory is full!",
                    "Purchase Failed",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Process the purchase
        int buyValue = item.getBuyValue();
        player.addGold(-buyValue);
        player.addItem(item);

        itemPanel.clearItem();

        // Update displays
        updateInventoryDisplay();
        updateGoldDisplay();
        updateRerollButton();
    }

    /**
     * Handles selling an item from the inventory.
     * Confirms the sale with the player before proceeding.
     *
     * @param inventoryIndex The index of the item in the inventory
     */
    private void handleItemSale(int inventoryIndex) {
        List<Item> inventory = player.getInventory();

        if (inventoryIndex >= 0 && inventoryIndex < inventory.size()) {
            Item item = inventory.get(inventoryIndex);

            int response = JOptionPane.showConfirmDialog(this,
                    "Sell " + item.getName() + " for " + item.getSellValue() + " gold?",
                    "Confirm Sale",
                    JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                player.sellItem(item);
                updateInventoryDisplay();
                updateGoldDisplay();
                updateRerollButton();
            }
        }
    }

    /**
     * Handles rerolling the shop inventory.
     * Confirms the reroll with the player and applies the cost.
     */
    private void handleShopReroll() {
        int rerollCost = calculateRerollCost();

        // Check if player has enough gold
        if (player.getGold() < rerollCost) {
            JOptionPane.showMessageDialog(this,
                    "Not enough gold to reroll the shop!",
                    "Reroll Failed",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Confirmation dialog
        int response = JOptionPane.showConfirmDialog(this,
                "Reroll the shop for " + rerollCost + " gold?",
                "Confirm Reroll",
                JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            // Deduct gold
            player.addGold(-rerollCost);

            // Increment reroll counter
            rerollCount++;

            // Generate new items
            generateShopItems();

            // Update displays
            updateGoldDisplay();
            updateRerollButton();
        }
    }

    /**
     * Calculates the gold cost for rerolling the shop.
     * Cost increases with each reroll.
     *
     * @return The gold cost for rerolling
     */
    private int calculateRerollCost() {
        // Cost formula: BASE_REROLL_COST + rerollCount
        return BASE_REROLL_COST + rerollCount;
    }

    //-------------------- UI Update Methods --------------------//

    /**
     * Updates the inventory display to reflect current items.
     * Sets up right-click listeners for selling items.
     */
    private void updateInventoryDisplay() {
        List<Item> inventory = player.getInventory();

        // Clear all slots first
        for (CircleItemSlot slot : inventorySlots) {
            slot.clearItem();
        }

        // Populate slots with inventory items
        for (int i = 0; i < inventory.size(); i++) {
            if (i < inventorySlots.size()) {
                inventorySlots.get(i).setItem(inventory.get(i));

                // Set up right-click listener for selling
                final int index = i;
                inventorySlots.get(i).setMouseListener(e -> {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        handleItemSale(index);
                    }
                });
            }
        }
    }

    /**
     * Updates the gold display to show current player gold.
     */
    private void updateGoldDisplay() {
        goldLabel.setText("Gold: " + player.getGold());
    }

    /**
     * Updates the reroll button text and enabled state
     * based on the current reroll cost and player's gold.
     */
    private void updateRerollButton() {
        int cost = calculateRerollCost();
        rerollButton.setText("Reroll Shop (" + cost + " gold)");
        rerollButton.setEnabled(player.getGold() >= cost);
    }

    /**
     * Returns to the main game and refreshes the game display.
     * Handles potential errors during transition.
     */
    private void returnToGame() {
        try {
            gameManager.continueFromShop();
            setVisible(false);
            dispose();
            parentFrame.setVisible(true);

            // Refresh the game display if returning to MainGameFrame
            if (parentFrame instanceof MainGameFrame) {
                ((MainGameFrame) parentFrame).refreshGameDisplay();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error returning to game: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}