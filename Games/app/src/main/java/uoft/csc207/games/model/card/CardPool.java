package uoft.csc207.games.model.card;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class stores every card that has been added to the game, with no duplicates, like a library
 * of cards, containing a list of every card and a selected card from the card pool
 */
public class CardPool implements Serializable {

    private List<Card> cardList;
    private Card selectedCard;

    /**
     * The cardList is first set to be empty and the selected card is an empty card
     */
    public CardPool() {
        cardList = new ArrayList<>(0);
        selectedCard = CardCollection.emptyCard;
    }

    public void addNewCard(Card card) {
        cardList.add(card);
    }

    public boolean cardExists(String cardName) {
        for (Card card : cardList) {
            if (card.getName().equals(cardName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Iterating through cardList to check whether any card in cardList has the same name as the
     * name of the searched card, if so, set the selected card of this CardPool to be said card and
     * return it
     * @param cardName
     * @return
     */
    public Card searchCard(String cardName) {
        for (Card card : cardList) {
            if (card.getName().equals(cardName)) {
                selectedCard = card;
            }
        }
        return selectedCard;
    }
}
