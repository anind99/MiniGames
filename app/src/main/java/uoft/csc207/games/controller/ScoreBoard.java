package uoft.csc207.games.controller;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import uoft.csc207.games.model.Ranker;
import uoft.csc207.games.model.dodger.Constants;


public class ScoreBoard implements Serializable {
    private ArrayList<Score> score_board;
    private Context context;
    private String NAME_OF_PROFILE_STORE = "Scoreboard";
    private Ranker ranker;
    private static Score currentScore = new Score("", 0 , 0, "");

    public ScoreBoard(){
        this.context = Constants.CURRENT_CONTEXT;
        score_board = new ArrayList<>();
        if (context != null) {
            loadScores();
        }
        this.ranker = new Ranker(score_board);
    }

    public static void setCurrentScore(Score s){
        currentScore = s;
    }
    public static Score getCurrentScore(){
        return currentScore;
    }

    public void submitScore(Score submit){
        score_board.add(submit);
    }

    public ArrayList<Score> sortScores(boolean sort_by_points){
        if (sort_by_points){
            ranker.createListByScore(0, ranker.scores.size() - 1);
        } else {
            ranker.createListByCurrency(0, ranker.scores.size() - 1);
        }
        return ranker.scores;
    }

    public void loadScores(){
        File dir2store = context.getFilesDir();
        String filePath = dir2store.getPath() + File.pathSeparator + NAME_OF_PROFILE_STORE;
        FileInputStream fileInputStream = null;
        try {
            File fileStoredProfiles = new File(filePath);
            if(!fileStoredProfiles.exists()){
                score_board = new ArrayList<>();
                return;
            }
            fileInputStream = new FileInputStream(new File(filePath));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            score_board = (ArrayList<Score>)objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * Serialize all scores to a system file
     */
    public void saveScores(){
        File dir2store = context.getFilesDir();
        String filePath = dir2store.getPath() + File.pathSeparator + NAME_OF_PROFILE_STORE;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(new File(filePath));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(score_board);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Score> getPlayerScores(String name){
        ArrayList<Score> player_scores = new ArrayList<>();
        for (Score s: score_board){
            if (s.getName().equals(name)){
                player_scores.add(s);
            }
        }
        return player_scores;
    }
}
