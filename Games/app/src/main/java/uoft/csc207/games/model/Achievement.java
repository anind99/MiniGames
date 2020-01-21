package uoft.csc207.games.model;

import java.io.Serializable;

/**
 * An Achievement object has a name, a description, and is attainable at a certain threshold of score and
 * currency.
 */
public class Achievement implements Serializable {
    private String achievementName;
    private String description;
    private int scoreThreshold;
    private int goldThreshold;
    private boolean checkGold;
    private boolean checkScore;

    /**
     * Creates an Achievement with all the necessary descriptive text and statistic thresholds to
     * achieve it.
     * @param name The name of the description
     * @param description The details of what the achievement is
     * @param scoreThreshold The minimum score needed to attain the achievement
     * @param goldThreshold The minimum gold needed to attain the achievement
     * @param checkScore Whether score is a condition of the achievement
     * @param checkGold Whether gold is a condition of the achievement
     */
    public Achievement(String name, String description, int scoreThreshold, int goldThreshold, boolean checkScore, boolean checkGold){
        achievementName = name;
        this.description = description;
        this.scoreThreshold = scoreThreshold;
        this.goldThreshold = goldThreshold;
        this.checkGold = checkGold;
        this.checkScore = checkScore;
    }

    /**
     * Checks if given score and gold values are high enough to attain a given Achievement.
     * @param score The score to be checked
     * @param gold The gold to be checked
     * @return Whether the given gold and score meet the requirements to attain the achievement
     */
    public boolean isAchieved(int score, int gold){
        boolean achieved = false;
        if (checkScore && !checkGold){
            achieved = (score >= scoreThreshold);
        } else if (checkGold && !checkScore){
            achieved = (gold >= goldThreshold);
        } else if (checkGold && checkScore){
            achieved = (score >= scoreThreshold) && (gold >= goldThreshold);
        }
        return achieved;
    }

    /**
     * Provides a String representation of the achievement for use in displaying a player's achievements
     * @return The achievement's name followed by its description on the next line
     */
    public String toString(){
        return achievementName + ":\n\t" + description;
    }
}
