package main.java.fr.ynov.ortalab.domain.checkers;

import main.java.fr.ynov.ortalab.domain.card.Card;
import main.java.fr.ynov.ortalab.domain.card.CardValue;
import main.java.fr.ynov.ortalab.domain.game.HandType;
import main.java.fr.ynov.ortalab.domain.utils.HandUtils;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * Checks for a Four of a Kind hand (four cards of the same value).
 * Also selects the highest remaining card as a kicker.
 */
public class FourOfAKindChecker implements HandChecker {

    @Override
    public boolean checkHand(List<Card> cards, Set<Card> usedCards, Set<Card> coreCards) {
        List<CardValue> fourOfAKindValues = HandUtils.findValueGroups(cards, 4);

        if (fourOfAKindValues.isEmpty()) {
            return false;
        }

        // Get the cards that make up the four of a kind
        CardValue quadsValue = fourOfAKindValues.getFirst();
        List<Card> quadsCards = cards.stream()
                .filter(card -> card.value() == quadsValue)
                .limit(4)
                .toList();

        usedCards.addAll(quadsCards);
        coreCards.addAll(quadsCards);

        // Find the highest kicker to complete the 5-card hand
        Set<CardValue> excludeValues = new HashSet<>();
        excludeValues.add(quadsValue);
        List<Card> kickers = HandUtils.getTopCardsByValue(cards, excludeValues, 1);

        if (!kickers.isEmpty()) {
            usedCards.add(kickers.getFirst());
        }

        return true;
    }

    @Override
    public HandType getHandType() {
        return HandType.FOUR_OF_A_KIND;
    }
}