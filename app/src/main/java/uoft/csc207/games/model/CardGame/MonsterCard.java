package uoft.csc207.games.model.CardGame;

public class MonsterCard extends Card {

    private int attack, defence;

    public MonsterCard(int attack, int defence, String name, int cardArt) {
        super(name, cardArt);
        this.defence = defence;
        this.attack = attack;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefence() {
        return defence;
    }
}
