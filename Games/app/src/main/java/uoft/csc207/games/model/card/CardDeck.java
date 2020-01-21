package uoft.csc207.games.model.card;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CardDeck implements DeckOrganizer, Serializable {

    private List<Card> deck;

    public CardDeck() {
        deck = new ArrayList<>(20);
    }

    @Override
    public void addThree(String cardName, CardPool cardPool) {
        if (cardPool.cardExists(cardName)) {
            Card selectedCard = cardPool.searchCard(cardName);
            for (int index : new int[]{1, 2, 3}) {
                if (deck.size() < 20) {
                    deck.add(selectedCard);
                }
            }
        }
    }

    @Override
    public void setDeckByList(String[] cardList, CardPool cardPool) {
        for (String cardName : cardList) {
            addThree(cardName, cardPool);
        }
    }

    public int getDeckSize() {
        return deck.size();
    }

    public Card getNextCard() {
        return deck.get(0);
    }

    public void removeNextCard() {
        deck.remove(0);
    }
}
