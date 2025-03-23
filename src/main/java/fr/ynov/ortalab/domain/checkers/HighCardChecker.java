package main.java.fr.ynov.ortalab.domain.checkers;

import main.java.fr.ynov.ortalab.domain.Card;
import main.java.fr.ynov.ortalab.domain.HandType;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class HighCardChecker implements HandChecker {
    @Override
    public boolean checkHand(List<Card> cards, Set<Card> usedCards, Set<Card> coreCards) {
        if (cards.isEmpty()) {
            return false;
        }

        // Find the highest card
        Card highestCard = cards.stream()
                .max(Comparator.comparingInt(card -> card.getValue().getNumericValue()))
                .orElse(null);

        if (highestCard == null) {
            return false;
        }

        // Add the highest card to both used and core cards
        usedCards.add(highestCard);
        coreCards.add(highestCard);

        // Add the next 4 highest cards to used cards only
        cards.stream()
                .filter(card -> !card.equals(highestCard))
                .sorted((c1, c2) -> c2.getValue().getNumericValue() - c1.getValue().getNumericValue())
                .limit(4)
                .forEach(usedCards::add);

        return true;
    }

    @Override
    public HandType getHandType() {
        return HandType.HIGH_CARD;
    }
}