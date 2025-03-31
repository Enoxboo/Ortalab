package main.java.fr.ynov.ortalab.gui.components;

import main.java.fr.ynov.ortalab.domain.game.Item;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.function.Consumer;

public class CircleItemSlot extends JPanel {
    private Item item;
    private boolean isHovered = false;
    private final int CIRCLE_SIZE = 60;
    private final Color EMPTY_COLOR = new Color(100, 100, 100);
    private final Color FILLED_COLOR = Color.WHITE;
    private final Color HOVER_COLOR = new Color(220, 220, 220);
    private JToolTip tooltip;

    public CircleItemSlot() {
        setPreferredSize(new Dimension(CIRCLE_SIZE, CIRCLE_SIZE));
        setToolTipText("");

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

    public void setItem(Item item) {
        this.item = item;
        updateTooltip();
        repaint();
    }

    public void clearItem() {
        this.item = null;
        updateTooltip();
        repaint();
    }

    private void updateTooltip() {
        if (item != null) {
            StringBuilder tooltipText = new StringBuilder("<html>");
            tooltipText.append("<b>").append(item.getName()).append("</b><br>");

            String effectType = getEffectTypeDescription();
            String effectValue = "+" + item.getValue();

            tooltipText.append(effectType).append(": ").append(effectValue);
            tooltipText.append("<br>Right-click to sell for ").append(item.getSellValue()).append(" gold");
            tooltipText.append("</html>");

            setToolTipText(tooltipText.toString());
        } else {
            setToolTipText("Empty Slot");
        }
    }

    private String getEffectTypeDescription() {
        if (item == null) return "";

        switch (item.getType()) {
            case SUIT_DAMAGE:
                if (item.getName().equals("The Moon")) {
                    return "Clubs damage bonus";
                } else if (item.getName().equals("The Sun")) {
                    return "Diamonds damage bonus";
                }
                return "Suit damage bonus";
            case HAND_TYPE_DAMAGE:
                return "Flush damage bonus";
            default:
                return "Unknown effect";
        }
    }

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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the circle
        if (item != null) {
            g2d.setColor(isHovered ? HOVER_COLOR : FILLED_COLOR);
        } else {
            g2d.setColor(EMPTY_COLOR);
        }

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