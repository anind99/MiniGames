package uoft.csc207.games.model.card;

public class SpellCard extends Card {

    private String spellEffect;
    private int effectValue;

    public SpellCard(String name, int cardArt, String spellEffect, int effectValue) {
        super(name, cardArt);
        this.spellEffect = spellEffect;
        this.effectValue = effectValue;
    }

    public String getSpellEffect() {
        return spellEffect;
    }

    public int getEffectValue() {
        return effectValue;
    }
}
