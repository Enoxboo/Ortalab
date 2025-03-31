package main.java.fr.ynov.ortalab.gui.components;

import main.java.fr.ynov.ortalab.domain.game.Item;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ShopItemPanel extends JPanel {
    private Item item;
    private final JLabel nameLabel;
    private final JLabel effectLabel;
    private final JLabel costLabel;
    private final JButton buyButton;
    private final JPanel circlePanel;
    private final int CIRCLE_SIZE = 60;
    private final Color CIRCLE_COLOR = Color.WHITE;

    public ShopItemPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));

        // Circle panel at the top
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

        // Add components to panel
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

        // Initially disable the button
        buyButton.setEnabled(false);
    }

    public void setItem(Item item) {
        this.item = item;

        if (item != null) {
            nameLabel.setText(item.getName());

            // Format effect description based on item type
            String effectType = getEffectTypeDescription();
            String effectValue = "+" + item.getValue();
            effectLabel.setText(effectType + ": " + effectValue);

            costLabel.setText("Cost: " + item.getBuyValue() + " gold");
            buyButton.setEnabled(true);
        } else {
            clearItem();
        }

        repaint();
    }

    private String getEffectTypeDescription() {
        if (item == null) return "";

        switch (item.getType()) {
            case SUIT_DAMAGE:
                if (item.getName().equals("The Moon")) {
                    return "Clubs damage";
                } else if (item.getName().equals("The Sun")) {
                    return "Diamonds damage";
                }
                return "Suit damage";
            case HAND_TYPE_DAMAGE:
                return "Flush damage";
            default:
                return "Unknown effect";
        }
    }

    public void clearItem() {
        this.item = null;
        nameLabel.setText("Sold Out");
        effectLabel.setText("");
        costLabel.setText("");
        buyButton.setEnabled(false);
        repaint();
    }

    public Item getItem() {
        return item;
    }

    public void addBuyButtonListener(ActionListener listener) {
        buyButton.addActionListener(listener);
    }
}