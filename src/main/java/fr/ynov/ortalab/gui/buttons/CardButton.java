package main.java.fr.ynov.ortalab.gui.buttons;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.CardSuit;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class CardButton extends JToggleButton {
    private final Card card;
    private static final int PREFERRED_WIDTH = 100;
    private static final int PREFERRED_HEIGHT = 150;
    private static final Font CARD_FONT = new Font("Arial", Font.BOLD, 25);
    private static final Color RED_SUIT_COLOR = Color.RED;
    private static final Color BLACK_SUIT_COLOR = Color.BLACK;
    private static final int CORNER_RADIUS = 20;

    public CardButton(Card card) {
        super(Objects.requireNonNull(card, "Card cannot be null").toShortString());
        this.card = card;

        setupButtonAppearance();
    }

    private void setupButtonAppearance() {
        setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
        setContentAreaFilled(false);
        setBackground(Color.WHITE);
        setFont(CARD_FONT);
        setForeground(determineForegroundColor());
    }

    private Color determineForegroundColor() {
        return (card.getSuit() == CardSuit.HEARTS || card.getSuit() == CardSuit.DIAMONDS)
                ? RED_SUIT_COLOR
                : BLACK_SUIT_COLOR;
    }

    public Card getCard() {
        return card;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Use selected state for background
        g2.setColor(getModel().isSelected() ? Color.LIGHT_GRAY : getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);

        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, CORNER_RADIUS, CORNER_RADIUS);
        g2.dispose();
    }

    @Override
    public void updateUI() {
        super.updateUI();
        setContentAreaFilled(false);
    }
}