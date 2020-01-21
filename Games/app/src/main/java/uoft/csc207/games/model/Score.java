package uoft.csc207.games.model;

import java.io.Serializable;

public class Score implements Serializable {
    /**
     * A class for one Score
     *
     * Fields
     * name: String - name of Score owner
     * points: int - Points scored
     * money: int = Money earned
     * class_name: String - Game Activity Name
     */
    private String name;
    private int points;
    private int money;
    private String class_name;

    public Score(String name, int points, int money, String class_name){
    this.money = money;
    this.name = name;
    this.points = points;
    this.class_name = class_name;}

    /**
     * @return money
     */
    public int getMoney() {
        return money;
    }

    /**
     * @return class_name
     */
    public String getClassName(){return class_name;}

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @return points
     */
    public int getPoints() {
        return points;
    }

    /**
     * @param name - set name (String)
     */
    public void setName(String name){
        this.name = name;
    }
}
