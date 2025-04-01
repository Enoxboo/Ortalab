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

public class DeckViewFrame extends JFrame {
    private final Deck deck;
    private final List<CardButton> cardButtons;
    private final JPanel deckPanel;

    public DeckViewFrame(Deck deck) {
        this.deck = deck;
        this.cardButtons = new ArrayList<>();

        setTitle("Deck View");
        setSize(1200, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        deckPanel = new JPanel(new GridLayout(4, 13, 5, 5));
        deckPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        createDeckView();
        add(new JScrollPane(deckPanel));
    }

    private void createDeckView() {
        // Create buttons for each card
        for (CardSuit suit : CardSuit.values()) {
            for (CardValue value : CardValue.values()) {
                Card card = new Card(value, suit);
                CardButton cardButton = createCardButton(card);
                deckPanel.add(cardButton);
                cardButtons.add(cardButton);
            }
        }
    }

    private CardButton createCardButton(Card card) {
        return new CardButton(card) {
            @Override
            protected void setupButtonAppearance() {
                super.setupButtonAppearance();
                // Make cards smaller
                setPreferredSize(new Dimension(80, 120));
                // Disable interactions
                setEnabled(false);
            }
        };
    }

    public void updateDeckView() {
        // Get the set of used cards from the deck
        Set<Card> usedCards = deck.getUsedCards();

        // Update each card button's appearance
        for (CardButton button : cardButtons) {
            Card card = button.getCard();

            // Grey out used cards
            if (usedCards.contains(card)) {
                button.setBackground(Color.LIGHT_GRAY);
            } else {
                button.setBackground(Color.WHITE);
            }

            // Ensure text color is reset
            button.setForeground(button.determineForegroundColor());
        }
    }
}