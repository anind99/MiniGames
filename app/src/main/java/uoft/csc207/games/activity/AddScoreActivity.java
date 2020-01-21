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
import uoft.csc207.games.controller.Score;
import uoft.csc207.games.controller.ScoreBoard;

public class AddScoreActivity extends AppCompatActivity {

    private Button submit;
    private Button cancel;
    private EditText gamer_tag;
    private TextView error_msg;
    private ScoreBoard scoreBoard = new ScoreBoard();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_add_score);
        gamer_tag = (EditText) findViewById(R.id.GamerTag);
        submit = (Button) findViewById(R.id.Submit);
        cancel = (Button) findViewById(R.id.Cancel);
        error_msg = (TextView) findViewById(R.id.error_msg);

        submit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Submit(gamer_tag.getText().toString(), ScoreBoard.getCurrentScore());
                moveToNextActivity();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                moveToNextActivity();
            }
        });

    }

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

