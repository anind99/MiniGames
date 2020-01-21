package uoft.csc207.games.model.card;

public interface SpellEffect {

    void destroyOneRandom();

    void destroyAll();

    void increaseHP(int healthPoint);

    void decreaseHP(int healthPoint);

    void attackAgain();
}
