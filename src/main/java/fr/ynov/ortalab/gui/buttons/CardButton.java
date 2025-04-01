package main.java.fr.ynov.ortalab.gui.buttons;

import main.java.fr.ynov.ortalab.domain.card.Card;
import main.java.fr.ynov.ortalab.domain.card.CardSuit;


import javax.swing.JToggleButton;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Optional;

public class CardButton extends JToggleButton {
    private final Card card;
    private static final int PREFERRED_WIDTH = 100;
    private static final int PREFERRED_HEIGHT = 150;
    private static final Font CARD_FONT = new Font("Arial", Font.BOLD, 25);
    private static final Color RED_SUIT_COLOR = Color.RED;
    private static final Color BLACK_SUIT_COLOR = Color.BLACK;
    private static final int CORNER_RADIUS = 20;
    private static final Color SELECTION_COLOR = new Color(173, 216, 230, 150);

    public CardButton(Card card) {
        super(card != null ? card.toShortString() : "");
        this.card = card;

        setupButtonAppearance();
    }

    protected void setupButtonAppearance() {
        setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
        setContentAreaFilled(false);
        setBackground(Color.WHITE);
        setFont(CARD_FONT);

        setForeground(determineForegroundColor());
    }

    public Color determineForegroundColor() {
        return Optional.ofNullable(card).filter(c -> (c.suit() == CardSuit.HEARTS || c.suit() == CardSuit.DIAMONDS)).map(c -> RED_SUIT_COLOR).orElse(BLACK_SUIT_COLOR);
    }

    public Card getCard() {
        return card;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Base background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);

        // Selection overlay
        if (isSelected()) {
            g2.setColor(SELECTION_COLOR);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);
        }

        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Border color changes when selected
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, CORNER_RADIUS, CORNER_RADIUS);

        g2.dispose();
    }

    @Override
    public void updateUI() {
        super.updateUI();
        setContentAreaFilled(false);
        setForeground(determineForegroundColor());
    }
}