package main.java.fr.ynov.ortalab.domain.checkers;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.CardSuit;
import main.java.fr.ynov.ortalab.domain.HandType;
import main.java.fr.ynov.ortalab.domain.utils.HandUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RoyalFlushChecker implements HandChecker {
    @Override
    public boolean checkHand(List<Card> cards, Set<Card> usedCards, Set<Card> coreCards) {
        List<Card> sortedCards = HandUtils.getSortedCards(cards);

        // Group cards by suit
        for (CardSuit suit : CardSuit.values()) {
            List<Card> sameSuitCards = sortedCards.stream()
                    .filter(card -> card.getSuit() == suit)
                    .collect(Collectors.toList());

            if (sameSuitCards.size() < 5) {
                continue;
            }

            boolean hasTen = false, hasJack = false, hasQueen = false,
                    hasKing = false, hasAce = false;
            List<Card> royalCards = new ArrayList<>();

            for (Card card : sameSuitCards) {
                switch (card.getValue()) {
                    case TEN -> { hasTen = true; royalCards.add(card); }
                    case JACK -> { hasJack = true; royalCards.add(card); }
                    case QUEEN -> { hasQueen = true; royalCards.add(card); }
                    case KING -> { hasKing = true; royalCards.add(card); }
                    case ACE -> { hasAce = true; royalCards.add(card); }
                }
            }

            if (hasTen && hasJack && hasQueen && hasKing && hasAce) {
                usedCards.addAll(royalCards);
                coreCards.addAll(royalCards);
                return true;
            }
        }

        return false;
    }

    @Override
    public HandType getHandType() {
        return HandType.ROYAL_FLUSH;
    }
}