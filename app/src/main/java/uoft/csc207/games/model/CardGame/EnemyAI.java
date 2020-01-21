package uoft.csc207.games.model.CardGame;

public class EnemyAI implements CardClicker {

    private CardDeck aiDeck;

    public EnemyAI() {
        aiDeck = new CardDeck();
    }

    @Override
    public void clickSummon(CardGameState cardGameState, int posIndex) {
        if (!cardGameState.getFullAiBoard().isOccupied(posIndex)) {
            if (cardGameState.getFullAiHand().isOccupied(posIndex)) {
                cardGameState.getFullAiBoard().setCard(posIndex, cardGameState.getAiHand(0));
                cardGameState.getFullAiHand().setCard(posIndex, CardCollection.emptyCard);
            }
        }
    }

    @Override
    public void clickAttack(CardGameState cardGameState, int posIndex, int targetPosIndex) {
        if (cardGameState.getFullAiBoard().isOccupied(posIndex)) {
            if (cardGameState.getFullPlayerBoard().isOccupied(targetPosIndex)) {
                int damageDifference =
                        ((MonsterCard) cardGameState.getAiBoard(posIndex)).getAttack() -
                                ((MonsterCard) cardGameState.getPlayerBoard(targetPosIndex)).getAttack();
                if (damageDifference > 0) {
                    cardGameState.getFullPlayerBoard().setCard(targetPosIndex, CardCollection.emptyCard);
                    cardGameState.attack(damageDifference, "player");
                } else if (damageDifference < 0) {
                    cardGameState.getFullAiBoard().setCard(posIndex, CardCollection.emptyCard);
                    cardGameState.attack(damageDifference, "ai");
                } else {
                    cardGameState.getFullAiBoard().setCard(posIndex, CardCollection.emptyCard);
                    cardGameState.getFullPlayerBoard().setCard(targetPosIndex, CardCollection.emptyCard);
                }
            } else {
                cardGameState.attack(
                        ((MonsterCard) cardGameState.getFullAiBoard().getCard(posIndex)).getAttack(),
                        "player");
            }
        }
    }

    @Override
    public void clickDirectAttack(CardGameState cardGameState, int posIndex) {

    }

    @Override
    public void clickTargetAttack(CardGameState cardGameState, int posIndex) {

    }

    @Override
    public void clickActivate(CardGameState cardGameState, int posIndex) {

    }

    public void setUpDeck(String cardName, CardPool cardpool) {
        //Sets up the EnemyAI deck
        aiDeck.addThree(cardName, cardpool);
        aiDeck.addThree(cardName, cardpool);
        aiDeck.addThree(cardName, cardpool);
        aiDeck.addThree(cardName, cardpool);
    }

    public CardDeck getAiDeck() {
        return aiDeck;
    }
}
