package uoft.csc207.games.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import uoft.csc207.games.R;
import uoft.csc207.games.controller.ProfileManager;
import uoft.csc207.games.model.Score;
import uoft.csc207.games.controller.scoreboard.ScoreBoard;

public class AddScoreActivity extends AppCompatActivity {
    /**
     * Activity to Submit Score
     *
     * Fields
     * submit: Button - button for submitting Score
     * cancel: Button - button for cancelling submission
     * gamerTag: EditText - text box for writing the gamer tag
     * error_msg: TextView - prompt on "No gamer tag"
     * scoreBoard: ScoreBoard - The SooreBoard to be submit to
     */

    private Button submit;
    private Button cancel;
    private EditText gamerTag;
    private TextView error_msg;
    private ScoreBoard scoreBoard = new ScoreBoard();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_add_score);
        gamerTag = (EditText) findViewById(R.id.GamerTag);
        submit = (Button) findViewById(R.id.Submit);
        cancel = (Button) findViewById(R.id.Cancel);
        error_msg = (TextView) findViewById(R.id.error_msg);

        //Submits Score
        submit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Submit(gamerTag.getText().toString(), ScoreBoard.getCurrentScore());
                moveToNextActivity();
            }
        });

        // Cancels Submission
        cancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                moveToNextActivity();
            }
        });

    }

    /**
     * @param name - name of player "Gamer Tag"
     * @param s - Score to be submitted
     */
    private void Submit(String name, Score s){
        if (name.equals("") && s.getName().equals("")){
            error_msg.setText("Gamer Tag is required");
            return;
        } else if (s.getName().equals("")){
            s.setName(name);
        }
        scoreBoard.submitScore(s);
        scoreBoard.saveScores();

    }

    /**
     * Return to game selection
     */
    private void moveToNextActivity(){
        Intent myIntent;
        if(ProfileManager.getProfileManager(this).isTwoPlayerMode()){
            myIntent = new Intent(AddScoreActivity.this, TurnDisplayActivity.class);
            myIntent.putExtra("SOURCE_ACTIVITY", ScoreBoard.getCurrentScore().getClassName());
            ProfileManager.getProfileManager(this).changeCurrentPlayer();
        } else {
            myIntent = new Intent(AddScoreActivity.this, GameSelectActivity.class);
        }
        startActivity(myIntent);
    }
}

