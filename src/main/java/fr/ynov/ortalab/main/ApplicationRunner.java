package main.java.fr.ynov.ortalab.main;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.CardSuit;
import main.java.fr.ynov.ortalab.domain.CardValue;
import main.java.fr.ynov.ortalab.domain.HandEvaluator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Main class to run the application.
 *
 * @author [Your Name]
 */
public class ApplicationRunner {
    public static void main(String[] args) {
        Card aceHearts = new Card(CardValue.FIVE, CardSuit.HEARTS);
        Card kingHearts = new Card(CardValue.QUEEN, CardSuit.DIAMONDS);
        Card queenHearts = new Card(CardValue.SEVEN, CardSuit.SPADES);
        Card jackHearts = new Card(CardValue.SEVEN, CardSuit.SPADES);
        Card tenHearts = new Card(CardValue.SEVEN, CardSuit.SPADES);

        List<Card> hand = List.of(aceHearts, kingHearts, queenHearts, jackHearts, tenHearts);

        HandEvaluator evaluator = new HandEvaluator(hand);
        System.out.println(evaluator.getHandType());
        System.out.println(evaluator.calculateTotalPoints());
    }
}