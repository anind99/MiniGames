package uoft.csc207.games.model.CardGame;

import java.util.ArrayList;
import java.util.List;

public class CardPool {

    private List<Card> cardList;
    private Card selectedCard;

    public CardPool() {
        cardList = new ArrayList<>(0);
    }

    public void addNewCard(Card card) {
        cardList.add(card);
    }

    public boolean cardExists(String cardName) {
        for (Card card: cardList) {
            if (card.getName().equals(cardName)) {
                return true;
            }
        }
        return false;
    }

    public Card searchCard(String cardName) {
        for (Card card: cardList) {
            if (card.getName().equals(cardName)) {
                selectedCard = card;
            }
        }
        return selectedCard;
    }
}
