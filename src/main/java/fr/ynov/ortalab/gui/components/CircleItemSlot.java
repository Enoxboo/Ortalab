package main.java.fr.ynov.ortalab.gui.components;

import main.java.fr.ynov.ortalab.domain.game.Item;

import javax.swing.JPanel;
import javax.swing.JToolTip;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.function.Consumer;

/**
 * A circular UI component that represents an item slot in the game's inventory.
 * Displays items visually with hover effects and tooltips showing item details.
 */
public class CircleItemSlot extends JPanel {
    // UI Constants
    private final int CIRCLE_SIZE = 60;
    private final Color EMPTY_COLOR = new Color(100, 100, 100);
    private final Color FILLED_COLOR = Color.WHITE;
    private final Color HOVER_COLOR = new Color(220, 220, 220);

    // Data
    private Item item;
    private boolean isHovered = false;
    private JToolTip tooltip;

    /**
     * Creates a new CircleItemSlot with default size and mouse hover behavior.
     */
    public CircleItemSlot() {
        setPreferredSize(new Dimension(CIRCLE_SIZE, CIRCLE_SIZE));
        setToolTipText("");

        // Add basic hover effect listener
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }

    /**
     * Sets the item to be displayed in this slot and updates the tooltip.
     *
     * @param item The item to display
     */
    public void setItem(Item item) {
        this.item = item;
        updateTooltip();
        repaint();
    }

    /**
     * Clears the current item from this slot.
     */
    public void clearItem() {
        this.item = null;
        updateTooltip();
        repaint();
    }

    /**
     * Sets a custom mouse listener that handles click events while preserving hover effects.
     *
     * @param clickHandler Consumer function that will be called with the MouseEvent when clicked
     */
    public void setMouseListener(Consumer<MouseEvent> clickHandler) {
        // Remove existing mouse listeners first
        for (MouseListener listener : getMouseListeners()) {
            if (listener instanceof MouseAdapter) {
                removeMouseListener(listener);
            }
        }

        // Add new mouse listener with hover effect + click handler
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                clickHandler.accept(e);
            }
        });
    }

    /**
     * Updates the tooltip text based on the current item.
     * Shows item details and sell value when an item is present.
     */
    private void updateTooltip() {
        if (item != null) {
            // Build HTML tooltip with item details
            StringBuilder tooltipText = new StringBuilder("<html>");
            tooltipText.append("<b>").append(item.getName()).append("</b><br>");

            String effectType = item.getDescription();
            String effectValue = "+" + item.getValue();

            tooltipText.append(effectType).append(": ").append(effectValue);
            tooltipText.append("<br>Right-click to sell for ").append(item.getSellValue()).append(" gold");
            tooltipText.append("</html>");

            setToolTipText(tooltipText.toString());
        } else {
            setToolTipText("Empty Slot");
        }
    }

    /**
     * Renders the circular item slot with appropriate colors based on state.
     * Empty slots are gray, filled slots are white, and hovered slots are light gray.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Choose color based on item presence and hover state
        if (item != null) {
            g2d.setColor(isHovered ? HOVER_COLOR : FILLED_COLOR);
        } else {
            g2d.setColor(EMPTY_COLOR);
        }

        // Draw the circle
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