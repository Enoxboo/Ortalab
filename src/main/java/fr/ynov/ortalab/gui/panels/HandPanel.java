package main.java.fr.ynov.ortalab.gui.panels;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.game.GameManager;
import main.java.fr.ynov.ortalab.gui.buttons.CardButton;
import main.java.fr.ynov.ortalab.domain.utils.CardSorter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HandPanel extends JPanel {
    private static final int MAX_HAND_SIZE = 8;
    private final List<Card> playerHand;
    private final List<CardButton> cardButtons;
    private SortType currentSortType = SortType.VALUE;

    // Enum to track current sorting type
    public enum SortType {
        VALUE,
        SUIT
    }

    public HandPanel(List<Card> initialHand) {
        this.playerHand = new ArrayList<>(validateInitialHand(initialHand));
        this.cardButtons = new ArrayList<>(MAX_HAND_SIZE);

        configureLayout();
        initializeHand();
    }

    private List<Card> validateInitialHand(List<Card> hand) {
        if (hand == null || hand.isEmpty()) {
            return generateNewHand();
        }

        // Ensure exactly 8 cards
        while (hand.size() < MAX_HAND_SIZE) {
            hand.add(GameManager.createRandomCard());
        }

        return hand.subList(0, MAX_HAND_SIZE);
    }

    private List<Card> generateNewHand() {
        return GameManager.generateInitialHand();
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
        playerHand.removeAll(cardsToRemove);
        playerHand.addAll(
                Collections.nCopies(
                        cardsToRemove.size(),
                        GameManager.createRandomCard()
                )
        );
        initializeHand();
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

    public SortType getCurrentSortType() {
        return currentSortType;
    }
}