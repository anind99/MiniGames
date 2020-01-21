package uoft.csc207.games.model;

/**
 * Contains the string identifiers for each type of game. Used to check if a given PlayerProfile already
 * has a Game object for a specific game.
 */
public interface IGameID {
    String RPG = "rpgGameID";
    String DODGER = "dodgerGameID";
    String CARD = "cardGameID";
}
