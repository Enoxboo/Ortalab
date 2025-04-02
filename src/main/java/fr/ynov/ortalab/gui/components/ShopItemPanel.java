package main.java.fr.ynov.ortalab.gui.components;

import main.java.fr.ynov.ortalab.domain.game.Item;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.Box;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.event.ActionListener;

/**
 * A panel that displays a shop item with its details and purchase button.
 * Used in the game's shop interface to present buyable items to the player.
 */
public class ShopItemPanel extends JPanel {
    // UI Constants
    private final int CIRCLE_SIZE = 60;
    private final Color CIRCLE_COLOR = Color.WHITE;

    // UI Components
    private final JPanel circlePanel;
    private final JLabel nameLabel;
    private final JLabel effectLabel;
    private final JLabel costLabel;
    private final JButton buyButton;

    // Data
    private Item item;

    /**
     * Creates a new ShopItemPanel with default layout and components.
     * The panel displays a circle representation of the item and its details.
     */
    public ShopItemPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));

        // Circle panel at the top to visually represent the item
        circlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (item != null) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // Draw white circle
                    g2d.setColor(CIRCLE_COLOR);
                    int x = (getWidth() - CIRCLE_SIZE) / 2;
                    int y = (getHeight() - CIRCLE_SIZE) / 2;
                    g2d.fillOval(x, y, CIRCLE_SIZE, CIRCLE_SIZE);

                    // Draw border
                    g2d.setColor(Color.BLACK);
                    g2d.setStroke(new BasicStroke(2.0f));
                    g2d.drawOval(x, y, CIRCLE_SIZE, CIRCLE_SIZE);

                    g2d.dispose();
                }
            }
        };
        circlePanel.setPreferredSize(new Dimension(200, 80));
        circlePanel.setMinimumSize(new Dimension(200, 80));
        circlePanel.setMaximumSize(new Dimension(200, 80));

        // Labels for item details
        nameLabel = new JLabel("", SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        effectLabel = new JLabel("", SwingConstants.CENTER);
        effectLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        effectLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        costLabel = new JLabel("", SwingConstants.CENTER);
        costLabel.setFont(new Font("Arial", Font.BOLD, 14));
        costLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Buy button
        buyButton = new JButton("Purchase");
        buyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buyButton.setFocusPainted(false);
        buyButton.setEnabled(false);  // Initially disabled until item is set

        // Assemble components with spacing
        add(Box.createVerticalStrut(10));
        add(circlePanel);
        add(Box.createVerticalStrut(10));
        add(nameLabel);
        add(Box.createVerticalStrut(5));
        add(effectLabel);
        add(Box.createVerticalStrut(10));
        add(costLabel);
        add(Box.createVerticalStrut(15));
        add(buyButton);
        add(Box.createVerticalStrut(10));
    }

    /**
     * Sets the item displayed in this panel and updates all UI components.
     *
     * @param item The item to display, or null to clear the panel
     */
    public void setItem(Item item) {
        this.item = item;

        if (item != null) {
            nameLabel.setText(item.getName());
            effectLabel.setText("<html><div style='text-align:center;'>" + item.getDescription());
            costLabel.setText("Cost: " + item.getBuyValue() + " gold");
            buyButton.setEnabled(true);
        } else {
            clearItem();
        }

        repaint();
    }

    /**
     * Clears the current item and updates the UI to show the slot is empty.
     */
    public void clearItem() {
        this.item = null;
        nameLabel.setText("Sold Out");
        effectLabel.setText("");
        costLabel.setText("");
        buyButton.setEnabled(false);
        repaint();
    }

    /**
     * Returns the currently displayed item.
     *
     * @return The current item, or null if no item is set
     */
    public Item getItem() {
        return item;
    }

    /**
     * Adds an action listener to the buy button.
     *
     * @param listener The action listener to add
     */
    public void addBuyButtonListener(ActionListener listener) {
        buyButton.addActionListener(listener);
    }
}