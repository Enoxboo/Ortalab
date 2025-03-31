package main.java.fr.ynov.ortalab.gui.frames;

import main.java.fr.ynov.ortalab.domain.game.Item;
import main.java.fr.ynov.ortalab.domain.game.ItemShop;
import main.java.fr.ynov.ortalab.domain.game.Player;
import main.java.fr.ynov.ortalab.domain.game.managers.GameManager;
import main.java.fr.ynov.ortalab.gui.components.CircleItemSlot;
import main.java.fr.ynov.ortalab.gui.components.ShopItemPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class ItemShopFrame extends JFrame {
    private static final int MAX_SHOP_ITEMS = 3;
    private static final int MAX_INVENTORY_SLOTS = 6;

    private final GameManager gameManager;
    private final Player player;
    private final ItemShop itemShop;
    private final List<ShopItemPanel> shopItemPanels = new ArrayList<>();
    private final List<CircleItemSlot> inventorySlots = new ArrayList<>();
    private JLabel goldLabel;
    private final JFrame parentFrame;

    private ItemShopFrame(GameManager gameManager, JFrame parentFrame) {
        this.gameManager = gameManager;
        this.player = gameManager.getPlayer();
        this.itemShop = new ItemShop(); // Create instance of ItemShop
        this.parentFrame = parentFrame;

        setTitle("Item Shop");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                returnToGame();
            }
        });

        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        // Main panel with shop items
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Shop panel (center)
        JPanel shopPanel = createShopPanel();
        mainPanel.add(shopPanel, BorderLayout.CENTER);

        // Inventory panel (right side)
        JPanel inventoryPanel = createInventoryPanel();
        mainPanel.add(inventoryPanel, BorderLayout.EAST);

        // Bottom panel with gold and exit button
        JPanel bottomPanel = createBottomPanel();

        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Generate random shop items using ItemShop class
        generateShopItems();

        // Update inventory display
        updateInventoryDisplay();

        // Update gold display
        updateGoldDisplay();
    }

    private JPanel createShopPanel() {
        JPanel shopPanel = new JPanel(new BorderLayout());
        shopPanel.setBorder(BorderFactory.createTitledBorder("Choose an Item"));

        JPanel itemsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));

        // Create shop item panels
        for (int i = 0; i < MAX_SHOP_ITEMS; i++) {
            ShopItemPanel itemPanel = new ShopItemPanel();
            itemPanel.setPreferredSize(new Dimension(200, 250));

            // Add buy button action listener
            itemPanel.addBuyButtonListener(e -> {
                handleItemPurchase(itemPanel);
            });

            shopItemPanels.add(itemPanel);
            itemsPanel.add(itemPanel);
        }

        shopPanel.add(itemsPanel, BorderLayout.CENTER);
        return shopPanel;
    }

    private JPanel createInventoryPanel() {
        JPanel inventoryPanel = new JPanel();
        inventoryPanel.setPreferredSize(new Dimension(250, 0));
        inventoryPanel.setBorder(BorderFactory.createTitledBorder("Inventory"));
        inventoryPanel.setLayout(new BoxLayout(inventoryPanel, BoxLayout.Y_AXIS));

        JPanel slotsPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        slotsPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        // Create inventory slots as circles
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

    private void generateShopItems() {
        // Get random items from the ItemShop class
        List<Item> randomItems = itemShop.getRandomItems(MAX_SHOP_ITEMS);

        // Display the random items in the shop panels
        for (int i = 0; i < Math.min(randomItems.size(), shopItemPanels.size()); i++) {
            shopItemPanels.get(i).setItem(randomItems.get(i));
        }
    }

    private void handleItemPurchase(ShopItemPanel itemPanel) {
        Item item = itemPanel.getItem();

        if (item == null) {
            return;
        }

        // Debug print
        System.out.println("Attempting to purchase: " + item.getName() + " for " + item.getBuyValue() + " gold");
        System.out.println("Current gold: " + player.getGold());

        // Check if player has enough gold
        if (player.getGold() < item.getBuyValue()) {
            JOptionPane.showMessageDialog(this,
                    "Not enough gold to purchase this item!",
                    "Purchase Failed",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check if inventory is full
        if (player.getInventory().size() >= MAX_INVENTORY_SLOTS) {
            JOptionPane.showMessageDialog(this,
                    "Inventory is full!",
                    "Purchase Failed",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Directly handle purchase - manually deduct gold and add item
        int buyValue = item.getBuyValue();
        player.addGold(-buyValue);
        player.addItem(item);

        // Debug print
        System.out.println("Purchase completed. New gold: " + player.getGold());

        // Remove item from shop display
        itemPanel.clearItem();

        // Update displays
        updateInventoryDisplay();
        updateGoldDisplay();
    }

    private void updateInventoryDisplay() {
        List<Item> inventory = player.getInventory();

        // Reset all slots
        for (CircleItemSlot slot : inventorySlots) {
            slot.clearItem();
        }

        // Fill slots with player's items
        for (int i = 0; i < inventory.size(); i++) {
            if (i < inventorySlots.size()) {
                inventorySlots.get(i).setItem(inventory.get(i));

                // Add sell action to slot (right-click)
                final int index = i;
                inventorySlots.get(i).setMouseListener(e -> {
                    // Handle right-click as sell
                    if (SwingUtilities.isRightMouseButton(e)) {
                        handleItemSale(index);
                    }
                });
            }
        }
    }

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
            }
        }
    }

    private void updateGoldDisplay() {
        // Debug print
        System.out.println("Updating gold display: " + player.getGold());
        goldLabel.setText("Gold: " + player.getGold());
    }

    private void returnToGame() {
        try {
            gameManager.continueFromShop();
            setVisible(false);
            dispose();
            parentFrame.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error returning to game: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void displayShopMidGame(GameManager gameManager, JFrame parentFrame) {
        ItemShopFrame shopFrame = new ItemShopFrame(gameManager, parentFrame);
        shopFrame.setVisible(true);
    }
}