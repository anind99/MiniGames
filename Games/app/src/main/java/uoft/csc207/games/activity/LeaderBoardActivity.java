package uoft.csc207.games.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import uoft.csc207.games.R;
import uoft.csc207.games.model.Score;
import uoft.csc207.games.controller.scoreboard.ScoreBoard;

public class LeaderBoardActivity extends AppCompatActivity {
    /**
     * Activity to view Score Board
     *
     * Fields
     * score_view: ListView - scores
     * scoreBoard: ScoreBoard - ScoreBoard
     * adapter: ArrayAdapter - adapter for List of Scores
     * change: Button - Button to prompt different sort
     * exit: Button - to return to game selection
     * score: ArrayList<Score> - list of scores in scoreBoard
     * sort_by_points - indicates sorting type
     */

    private ListView score_view;
    private ScoreBoard scoreBoard = new ScoreBoard();
    private ArrayAdapter adapter;
    private Button change;
    private Button exit;
    private ArrayList<Score> scores;
    private boolean sort_by_points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_scores);
        score_view = (ListView)findViewById(R.id.score_board);
        sort_by_points = true;
        change = (Button)findViewById(R.id.Change);
        exit = (Button)findViewById(R.id.score_exit);

        // change sorting
        change.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                sort_by_points = !sort_by_points;
                scores = scoreBoard.sortScores(sort_by_points);
                adapter = new ArrayAdapter(LeaderBoardActivity.this, android.R.layout.simple_list_item_1, viewScore(scores));
                score_view.setAdapter(adapter);
            }
        });

        //exit screen
        exit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(LeaderBoardActivity.this, GameSelectActivity.class);
                startActivity(intent);
            }
        });

        scores = scoreBoard.sortScores(sort_by_points);
        adapter = new ArrayAdapter(LeaderBoardActivity.this, android.R.layout.simple_list_item_1, viewScore(scores));
        score_view.setAdapter(adapter);
    }

    /**
     * Creates a String list from Score List
     * @param score_all: ArrayList<Score> - Score List
     * @return ArrayList<String> of scores
     */
    private ArrayList<String> viewScore(ArrayList<Score> score_all){
        ArrayList<String> return_list = new ArrayList<>();
        String sorted_by = "Points:";
        if (!sort_by_points){
            sorted_by = "Coins";
        }
        return_list.add("Scores Sorted By "+sorted_by);
        for (int i = score_all.size() - 1; i >= 0; i--){
            Score s = score_all.get(i);
            String g;
            if (s.getClassName().split("[.]").length > 0) {
                g = s.getClassName().split("[.]")[s.getClassName().split("[.]").length - 1];
            } else{
                g = s.getClassName();
            }
            return_list.add(s.getName() + ": Points - "+s.getPoints()+" Coins Earned - "+s.getMoney() +" Game: "+g);
        }
        return return_list;
    }

}
