package uoft.csc207.games.model.CardGame;

import uoft.csc207.games.R;

/***
 * A collection of cards, typically used for a hand
 */
public class CardCollection {
    private Card[] cards;

    static final Card emptyCard = new Card("Empty Slot", R.drawable.square);

    /***
     * Initialize a new empty collection with len card slots
     *
     * @param len the number of card slots this hand should have
     */
    public CardCollection(int len) {
        cards = new Card[len];

        for (int i = 0; i < len; i++) {
            cards[i] = emptyCard;
        }
    }

    /**
     * Return the card at index
     *
     * @param index the index of the card to be returned
     * @return the card at index
     */
    public Card getCard(int index) {
        return cards[index];
    }

    /**
     * Set the card at index to c, returning the original card at that position, or emptyCard if
     * unoccupied
     *
     * @param c     the new card to be placed at index
     * @param index the index where c should be inserted
     * @return the card formerly at index
     */
    public Card setCard(int index, Card c) {
        Card prev = cards[index];
        cards[index] = c;
        return prev;
    }

    /**
     * Return the card at index, removing it from collection
     *
     * @param index the index of the card to be removed
     * @return the card at index
     */
    public Card pop(int index) {
        Card result = cards[index];
        cards[index] = emptyCard;
        return result;
    }

    /***
     * Returns whether there is a card at index
     *
     * @param index the index to check
     * @return whether there is a card at index
     */
    public boolean isOccupied(int index) {
        return cards[index] != emptyCard;
    }

    /**
     * Returns the number of cards this collection can hold
     *
     * @return the number of cards this collection can hold
     */
    public int getSize() {
        return cards.length;
    }

    public int getOccupiedSize() {
        int occupiedNumber = 0;
        for (Card card: cards) {
            if (!card.equals(emptyCard)) {
                occupiedNumber++;
            }
        }
        return occupiedNumber;
    }
}
