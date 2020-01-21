package uoft.csc207.games.model.card;

public interface CardClicker {

    void clickSummon(CardGameState cardGameState, int posIndex);

    void clickDirectAttack(CardGameState cardGameState, int posIndex);

    void clickTargetAttack(CardGameState cardGameState, int posIndex);

    void clickAttack(CardGameState cardGameState, int posIndex, int targetPosIndex);

    void clickActivate(CardGameState cardGameState, int posIndex);
}
