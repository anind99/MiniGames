package uoft.csc207.games.model.rpg;

import uoft.csc207.games.model.Achievement;
import uoft.csc207.games.model.Game;
import uoft.csc207.games.model.IGameID;

/**
 * Contains the relevant game statistics and customization options of the Rpg game
 */
public class RpgGameState extends Game {
    public final static String FONT_TYPE_MONOSPACE = "monospace";
    public final static String FONT_TYPE_SANS_SERIF = "sans-serif";
    public final static String FONT_COLOR_BLACK = "black";
    public final static String FONT_COLOR_WHITE = "white";
    public final static String FONT_COLOR_RED = "red";

    /**
     * Initializes a default RpgGameState with an id shared by all RpgGameStates and a set of achievements
     * specific to the rpg game
     */
    public RpgGameState(){
        super();
        id = IGameID.RPG;
        initializeAchievements();
    }

    public String getId(){
        return id;
    }

    /**
     * Updates both the current playthrough's score and the total score
     * @param i The amount to add to each score variable
     */
    public void updateScore(Integer i){
        gameScore += i;
        cumulativeScore += i;
    }

    /**
     * Updates both the current playthrough's currency and the total currency
     * @param i The amount to add to each currency variable
     */
    public void updateCurrency(Integer i){
        gameCurrency += i;
        cumulativeCurrency += i;
    }

    /**
     * Resets the gameCurrency and gameScore to 0, the cumulative stats aren't affected
     */
    public void restart(){
        gameCurrency = 0;
        gameScore = 0; 
    }
    public void chooseCharacter(String character){
        this.character = character;
    }
    public void chooseFont(String font){
        this.textFont = font;
    }
    public void chooseColor(String color){
        this.color = color;
    }

    /**
     * Initializes all the attainable achievements in the rpg game
     */
    public void initializeAchievements(){
        Achievement temp;
        temp = new Achievement("Adventurer", "\tAchieve score of at least 100 in the Rpg.",
                100, 0, true, false);
        availableAchievements.add(temp);
        temp = new Achievement("Talkative", "\tAchieve score of at least 200 in the Rpg",
                200, 0, true, false);
        availableAchievements.add(temp);
        temp = new Achievement("Moving up in the world", "\tGet 1 gold.",
                0, 1, false, true);
        availableAchievements.add(temp);
        temp = new Achievement("Enough for a meal", "\tGet 10 gold.", 0,
                10, false, true);
    }
}
