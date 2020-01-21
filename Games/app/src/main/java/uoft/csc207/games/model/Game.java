package uoft.csc207.games.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Game implements Serializable {
    /**
     * Abstract Super Class for all games, functions as a container for most if not all of the
     * relevant game information (information relevant to the player)
     *
     * Fields
     * gameScore: Integer - Current score of the game
     * gameCurrency: Integer - Current currency of the game
     * cumulativeScore: Integer - Score earned across all playthroughs
     * cumulativeCurrency: Integer - Currency earned across all playthroughs
     * availableAchievements: ArrayList<availableAchievements> - Collection of all
     *                     achievements that are still available to be achieved in this game.
     * completedAchievements: ArrayList<availableAchievements> - Collection of all
     *                          achievements that have been achieved .
     * id: String - ID of the specific type of game (ex. all CardGame's have the same id)
     * color: String - String representing the color customisation of Game
     * character: String - String representing the character customisation of Game
     * textFont: String - String representing the font customisation of Game
     */

    protected Integer gameScore;
    protected Integer gameCurrency;
    protected Integer cumulativeScore;
    protected Integer cumulativeCurrency;
    protected List<Achievement> availableAchievements;
    private List<Achievement> completedAchievements;
    protected String id;
    protected String color;
    protected String character;
    protected String textFont;

    /**
     * Creates a Game with all the game statistics being 0 by default.
     */
    public Game(){
        gameScore = 0;
        gameCurrency = 0;
        cumulativeScore = 0;
        cumulativeCurrency = 0;
        availableAchievements = new ArrayList<>();
        completedAchievements = new ArrayList<>();
    }

    /**
     * @return The list of all attained achievements
     */
    ArrayList<Achievement> getCompletedAchievements() {
        return (ArrayList<Achievement>) completedAchievements;
    }
    /**
     * @return The identifier of what type of game it is
     */
    public abstract String getId();

    /**
     * Updates score of the Game and the total score of the account
     * @param i The amount to add to the score
     */
    public abstract void updateScore(Integer i);

    /**
     * Updates currency of the Game and the total currency of the account
     * @param i The amount to add to the currency
     */
    public abstract void updateCurrency(Integer i);

    /**
     * @param i: The new cumulative currency
     */
    public void setCumulativeCurrency(Integer i){ cumulativeCurrency = i; }

    /**
     * @return The total currency earned in the game
     */
    public int getCumulativeCurrency(){ return cumulativeCurrency; }

    /**
     * @param i: Cumulative Score to be set in game
     */
    public void setCumulativeScore(Integer i){ cumulativeScore = i; }

    /**
     * @return Gets the score across all playthroughs
     */
    public int getCumulativeScore(){ return cumulativeScore; }

    /**
     * Clears the game stats
     */
    public abstract void restart();

    /**
     * @param character The string representation of the character to be chosen
     */
    public abstract void chooseCharacter(String character);

    /**
     * @param font font to be chosen
     */
    public abstract void chooseFont(String font);

    /**
     * @param color to be chosen
     */
    public abstract void chooseColor(String color);

    /**
     * @return The score of the current playthrough
     */
    public int getScore(){
        return gameScore;
    }

    /**
     * @return The currency of the current game playthrough
     */
    public int getGameCurrency(){
        return gameCurrency;
    }

    /**
     * @return String representation of the character chosen
     */
    public String getCharacter(){
        return character;
    }

    /**
     * @return String representation of the text font customization option
     */
    public String getFont(){
        return textFont;
    }

    /**
     * @return String representation of the color customization option
     */
    public String getColor(){
        return color;
    }

    /**
     * Initialize all the achievements that can be attained in the game
     */
    public abstract void initializeAchievements();

    /**
     * Checks if any of the availableAchievements are satisfied, and moves any that are to the
     * completedAchievements array list so it's no longer checked.
     */
    public void checkAchievements(){
        Iterator<Achievement> itr = availableAchievements.iterator();
        while(itr.hasNext()){
            Achievement curAchievement = itr.next();
            if (curAchievement.isAchieved(gameScore, gameCurrency)){
                completedAchievements.add(curAchievement);
                itr.remove();
            }
        }
    }
}
