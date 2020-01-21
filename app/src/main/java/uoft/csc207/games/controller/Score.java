package uoft.csc207.games.controller;

import java.io.Serializable;

public class Score implements Serializable {
    private String name;
    private int points;
    private int money;
    private String class_name;

    public Score(String name, int points, int money, String class_name){
    this.money = money;
    this.name = name;
    this.points = points;
    this.class_name = class_name;}

    public int getMoney() {
        return money;
    }

    public String getClassName(){return class_name;}
    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }
    public void setName(String name){
        this.name = name;
    }
}
