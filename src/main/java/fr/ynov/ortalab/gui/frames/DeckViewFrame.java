package main.java.fr.ynov.ortalab.gui.frames;

import main.java.fr.ynov.ortalab.domain.card.Card;
import main.java.fr.ynov.ortalab.domain.card.CardSuit;
import main.java.fr.ynov.ortalab.domain.card.CardValue;
import main.java.fr.ynov.ortalab.domain.game.Deck;
import main.java.fr.ynov.ortalab.gui.buttons.CardButton;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A visual representation of a card deck that displays all cards in a grid layout.
 * Cards that have been used by the deck are visually differentiated.
 */
public class DeckViewFrame extends JFrame {
    private final Deck deck;
    private final List<CardButton> cardButtons;
    private final JPanel deckPanel;

    /**
     * Creates a new deck view frame for the specified deck.
     *
     * @param deck The deck to be visualized
     */
    public DeckViewFrame(Deck deck) {
        this.deck = deck;
        this.cardButtons = new ArrayList<>();

        setTitle("Deck View");
        setSize(1200, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create a grid layout with spaces between cards (4 suits × 13 values)
        deckPanel = new JPanel(new GridLayout(4, 13, 5, 5));
        deckPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        createDeckView();
        add(new JScrollPane(deckPanel));
    }

    /**
     * Creates card buttons for all possible cards and adds them to the panel.
     */
    private void createDeckView() {
        for (CardSuit suit : CardSuit.values()) {
            for (CardValue value : CardValue.values()) {
                Card card = new Card(value, suit);
                CardButton cardButton = createCardButton(card);
                deckPanel.add(cardButton);
                cardButtons.add(cardButton);
            }
        }
    }

    /**
     * Creates a custom CardButton with specific appearance settings.
     *
     * @param card The card to create a button for
     * @return A configured CardButton instance
     */
    private CardButton createCardButton(Card card) {
        return new CardButton(card) {
            @Override
            protected void setupButtonAppearance() {
                super.setupButtonAppearance();
                // Make cards smaller for better fit in the grid
                setPreferredSize(new Dimension(80, 120));
                // Disable interactions as this is view-only
                setEnabled(false);
            }
        };
    }

    /**
     * Updates the visual state of all cards to reflect the current deck state.
     * Used cards are grayed out while available cards remain white.
     */
    public void updateDeckView() {
        Set<Card> usedCards = deck.getUsedCards();

        for (CardButton button : cardButtons) {
            Card card = button.getCard();

            // Apply visual distinction between used and available cards
            if (usedCards.contains(card)) {
                button.setBackground(Color.LIGHT_GRAY);
            } else {
                button.setBackground(Color.WHITE);
            }

            // Ensure proper text color is maintained
            button.setForeground(button.determineForegroundColor());
        }
    }
}