package uoft.csc207.games.model.card;

public interface DeckOrganizer {
    void addThree(String cardName, CardPool cardPool);

    void setDeckByList(String[] cardList, CardPool cardPool);
}
