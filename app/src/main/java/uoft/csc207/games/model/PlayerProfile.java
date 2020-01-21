package uoft.csc207.games.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

public class PlayerProfile implements Serializable {
    private String id;
    private String password;
    private ArrayList<Game> games;

    /*private Integer playerCurrency;
    private Integer playerScore;
    private ArrayList<Achievement> playerAchievements;*/

    public PlayerProfile(String id, String password) {
        this.id = id;
        this.password = password;
        /*playerCurrency = 0;
        playerScore = 0;
        playerAchievements = new ArrayList<>();*/
        games = new ArrayList<>();
    }

    public String getId() { return id; }

    public String getPassword() {
        return password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCurrency(){
        int totalCurrency = 0;
        for (Game g: games){
            totalCurrency += g.getCumulativeCurrency();
        }
        return totalCurrency;
    }

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

    public int getNumAchievements(){
        int i = 0;
        for (Game g: games){
            ArrayList<Achievement> achievements = g.getCompletedAchievements();
            for (Achievement a: achievements){
                i++;
            }
        }
        return i;
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
