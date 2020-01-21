package uoft.csc207.games.model;


import java.io.Serializable;

public class ScrollerGame extends Game implements Serializable{



    public ScrollerGame(){
        super();
        initializeAchievements();
        this.id = IGameID.DODGER;
        color = null;
        character = null;
        textFont = null;
    }


    public String getId(){return this.id;}

    /**
     * Updates score of the Game and the total score of the account
     * @param i The amount to add to the score
     */
    public void updateScore(Integer i){
        cumulativeScore += i - gameScore;
        this.gameScore = i;
    }

    /**
     * Updates currency of the Game and the total currency of the account
     * @param i The amount to add to the currency
     */
    public void updateCurrency(Integer i){
        cumulativeCurrency += i - gameCurrency;
        this.gameCurrency = i;
    }

    public Integer getCurrency(){
        return this.gameCurrency;
    }


    public int getScore(){
        return this.gameScore;
    }


    /**
     * Clears the game stats
     */
    public void restart(){gameScore = 0;}

    public void chooseCharacter(String character){this.character = character;}

    public void chooseFont(String font){this.textFont = font;}


    public void chooseColor(String color){this.color = color;}

    /**
     * Initialize all the achievements that can be attained in your game
     */
    public void initializeAchievements(){
        for (int i = 50; i < 15000; i = i * 2){
            String name = "Scored: " + i;
            String description = "Player reached this score.";
            availableAchievements.add(new Achievement(name, description, i, 0, true, false));
        }
    }
}
