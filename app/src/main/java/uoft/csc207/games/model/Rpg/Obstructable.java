package uoft.csc207.games.model.Rpg;

/**
 * For GameObjects that are capable of blocking the PlayerCharacter. Implemented by objects that extend
 * GameObject that aren't PlayerCharacter.
 */
public interface Obstructable {
    /**
     * The first obstruction of the class implementing this interface doesn't mean the first time it has
     * obstructed a PlayerCharacter, but whether a given instant of time is the first point of contact/obstruction
     */
    boolean isFirstObstruction();
    void setFirstObstruction(boolean b);
}
