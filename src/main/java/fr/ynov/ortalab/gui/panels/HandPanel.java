package main.java.fr.ynov.ortalab.gui.panels;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.game.managers.GameManager;
import main.java.fr.ynov.ortalab.gui.buttons.CardButton;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HandPanel extends JPanel {
    private static final int MAX_HAND_SIZE = 8;
    private final List<Card> playerHand;
    private final List<CardButton> cardButtons;
    private final GameManager gameManager;
    private SortType currentSortType = SortType.VALUE;
    private final List<Runnable> selectionListeners = new ArrayList<>();

    public enum SortType {
        VALUE,
        SUIT
    }

    public HandPanel(List<Card> initialHand, GameManager gameManager) {
        this.gameManager = gameManager;
        this.playerHand = new ArrayList<>(validateInitialHand(initialHand));
        this.cardButtons = new ArrayList<>(MAX_HAND_SIZE);

        configureLayout();
        sortHand(currentSortType);
    }

    private List<Card> validateInitialHand(List<Card> hand) {
        if (hand == null || hand.isEmpty()) {
            return generateNewHand();
        }

        // Ensure exactly 8 cards using the deck
        while (hand.size() < MAX_HAND_SIZE) {
            hand.add(gameManager.getDeck().drawCard());
        }

        return hand.subList(0, MAX_HAND_SIZE);
    }

    private List<Card> generateNewHand() {
        List<Card> newHand = new ArrayList<>();
        for (int i = 0; i < MAX_HAND_SIZE; i++) {
            newHand.add(gameManager.getDeck().drawCard());
        }
        return newHand;
    }

    private void configureLayout() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
    }

    private void initializeHand() {
        cardButtons.clear();
        removeAll();

        playerHand.forEach(this::addCardButton);
        revalidate();
        repaint();
    }

    private void addCardButton(Card card) {
        CardButton cardButton = new CardButton(card);
        cardButton.addActionListener(e -> {
            for (Runnable listener : selectionListeners) {
                listener.run();
            }
        });

        cardButtons.add(cardButton);
        add(cardButton);
    }

    public List<Card> getSelectedCards() {
        return cardButtons.stream()
                .filter(AbstractButton::isSelected)
                .map(CardButton::getCard)
                .toList();
    }

    public void removeCards(List<Card> cardsToRemove) {
        // Remove selected cards from hand
        playerHand.removeAll(cardsToRemove);

        // Draw unique replacement cards from the deck
        List<Card> newCards = gameManager.getDeck().drawUniqueCards(cardsToRemove.size());
        playerHand.addAll(newCards);

        sortHand(currentSortType);

    }

    public List<Card> getPlayerHand() {
        return Collections.unmodifiableList(playerHand);
    }

    public void sortHand(SortType sortType) {
        // Update the current sort type
        this.currentSortType = sortType;

        // Sort the cards based on the selected type
        switch (sortType) {
            case VALUE:
                playerHand.sort((c1, c2) ->
                        Integer.compare(c1.getValue().getNumericValue(), c2.getValue().getNumericValue()));
                break;
            case SUIT:
                playerHand.sort((c1, c2) -> {
                    int suitCompare = c1.getSuit().compareTo(c2.getSuit());
                    if (suitCompare != 0) return suitCompare;
                    return Integer.compare(c1.getValue().getNumericValue(), c2.getValue().getNumericValue());
                });
                break;
        }

        // Refresh the hand display
        initializeHand();
    }

    public void addCardSelectionListener(Runnable listener) {
        selectionListeners.add(listener);
    }

    public SortType getCurrentSortType() {
        return currentSortType;
    }
}
