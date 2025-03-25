package main.java.fr.ynov.ortalab.game;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.CardSuit;
import main.java.fr.ynov.ortalab.domain.CardValue;
import main.java.fr.ynov.ortalab.domain.PointsCalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameManager {
    private static final int MAX_HAND_SIZE = 8;
    private static final Random random = new Random();

    /**
     * Creates a random card
     * @return A randomly generated Card
     */
    public static Card createRandomCard() {
        CardValue[] values = CardValue.values();
        CardSuit[] suits = CardSuit.values();

        CardValue value = values[random.nextInt(values.length)];
        CardSuit suit = suits[random.nextInt(suits.length)];

        return new Card(value, suit);
    }

    /**
     * Generates an initial hand of cards
     * @return A list of cards with exactly MAX_HAND_SIZE cards
     */
    public static List<Card> generateInitialHand() {
        List<Card> hand = new ArrayList<>();
        while (hand.size() < MAX_HAND_SIZE) {
            hand.add(createRandomCard());
        }
        return hand;
    }

    /**
     * Manages card replacement when discarding
     * @param currentHand The current hand of cards
     * @param cardsToRemove Cards to be removed and replaced
     * @return Updated hand with replaced cards
     */
    public static List<Card> replaceCards(List<Card> currentHand, List<Card> cardsToRemove) {
        List<Card> updatedHand = new ArrayList<>(currentHand);

        // Remove specified cards
        updatedHand.removeAll(cardsToRemove);

        // Add new random cards to maintain MAX_HAND_SIZE
        while (updatedHand.size() < MAX_HAND_SIZE) {
            updatedHand.add(createRandomCard());
        }

        return updatedHand;
    }

    /**
     * Calculates the score for played cards
     * @param selectedCards Cards selected to play
     * @return Calculated score
     */
    public static int calculatePlayScore(List<Card> selectedCards) {
        return PointsCalculator.calculateScore(selectedCards);
    }

    /**
     * Creates a sample initial hand for demonstration
     * @return A predefined list of cards
     */
    public static List<Card> createSampleHand() {
        List<Card> hand = new ArrayList<>();
        hand.add(new Card(CardValue.ACE, CardSuit.HEARTS));
        hand.add(new Card(CardValue.TEN, CardSuit.CLUBS));
        hand.add(new Card(CardValue.TEN, CardSuit.DIAMONDS));
        hand.add(new Card(CardValue.TEN, CardSuit.SPADES));
        hand.add(new Card(CardValue.TEN, CardSuit.HEARTS));
        hand.add(new Card(CardValue.NINE, CardSuit.DIAMONDS));
        hand.add(new Card(CardValue.NINE, CardSuit.SPADES));
        hand.add(new Card(CardValue.NINE, CardSuit.HEARTS));
        return hand;
    }
}