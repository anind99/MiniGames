package uoft.csc207.games.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Functions as the "account" of the player. Contains the account's username (id) and password, and
 * a list of Game objects, each of which contain the information of that player for a particular game.
 */
public class PlayerProfile implements Serializable {
    private String id;
    private String password;
    private ArrayList<Game> games;

    /**
     * Creates a PlayerProfile with a given username and password
     * @param id The username of the new PlayerProfile
     * @param password The password of the new PlayerProfile
     */
    public PlayerProfile(String id, String password) {
        this.id = id;
        this.password = password;
        games = new ArrayList<>();
    }

    public String getId() { return id; }

    public String getPassword() {
        return password;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Calculates the PlayerProfile's currency based on the total currency of each game it has played
     * @return The sum of the total currency earned in each game
     */
    public int getCurrency(){
        int totalCurrency = 0;
        for (Game g: games){
            totalCurrency += g.getCumulativeCurrency();
        }
        return totalCurrency;
    }

    /**
     * Calculates the PlayerProfiles score based on the total score earned across each game it has played
     * @return The sum of the total scores earned in each game
     */
    public int getScore(){
        int totalScore = 0;
        for (Game g: games){
            totalScore += g.getCumulativeScore();
        }
        return totalScore;
    }

    /**
     * Gets all the player's current achievements. Originally created this for display purposes when
     * the user clicks the profile button in GameSelectActivity, might change it to return a collection
     * of Achievements instead.
     * @return Returns a String representation of all the achievements
     */
    public String getAchievements(){
        String result = "";
        for (Game g: games){
            ArrayList<Achievement> achievements = g.getCompletedAchievements();
            for (Achievement a: achievements){
                result += a + "\n";
            }
        }
        return result;
    }

    /**
     * Checks if the PlayerProfile already owns the game
     * @param gameId The id of the game that is being searched for
     * @return The game with the given id and null if the PlayerProfile doesn't contain it
     */
    public Game containsGame(String gameId){
        for (Game g: games){
            if (g.getId().equals(gameId)){
                return g;
            }
        }
        return null;
    }

    /**
     *  Adds a game to the PlayerProfile's collection. Each profile should only have one instance of
     *  each type of game at most.
     * @param newGame The new instance of the Game to be added
     */
    public void addGame(Game newGame){
        if (containsGame(newGame.getId()) == null){
            games.add(newGame);
        }
    }
}
