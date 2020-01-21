package uoft.csc207.games.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uoft.csc207.games.R;
import uoft.csc207.games.controller.ProfileManager;
import uoft.csc207.games.activity.rpg.RpgActivity;
import uoft.csc207.games.activity.dodger.ScrollerActivity;
import uoft.csc207.games.model.CardGame.CardActivity;

/**
 * All the logic is in TurnDisplayActivity. Each game's activity only needs to check ProfileManager for
 * if it's two player mode, and if it is, putExtra the class name of that game's activity, switch the player's
 * turns, and move from their game's activity to TurnDisplayActivity. About 5 lines of code.
 */
public class TurnDisplayActivity extends AppCompatActivity{
    private TextView currentTurnDisplay;
    private Class destination;
    private List<Class> gameActivities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn_display);
        currentTurnDisplay = findViewById(R.id.tvCurrentTurn);
        currentTurnDisplay.setText("User " + ProfileManager.getProfileManager(this).getCurrentPlayer().getId() + "'s Turn");
        initClassTypeLists();
    }
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            Bundle extras = getIntent().getExtras();
            Intent myIntent;
            String sourceClassName = extras.getString("SOURCE_ACTIVITY");
            Class sourceClass = null;
            try{
                sourceClass = Class.forName(sourceClassName);
            } catch (ClassNotFoundException e){
            }

            if (ProfileManager.getProfileManager(this).isFirstTurn()){
                myIntent = new Intent(TurnDisplayActivity.this, sourceClass);
                startActivity(myIntent);
            } else {
                goToRandomGame(sourceClass);
            }
            return true;
        } else {
            return false;
        }
    }

    private void initClassTypeLists(){
        gameActivities = new ArrayList<>();
        gameActivities.add(RpgActivity.class);
        gameActivities.add(ScrollerActivity.class);
        gameActivities.add(CardActivity.class);
    }

    private void goToRandomGame(Class previousGame){
        Class[] gameActivityNames = gameActivities.toArray(new Class[0]);
        Intent myIntent;
        Class temp;
        int i;
        do{
            i = (int) (Math.random() * gameActivityNames.length);
        } while ((temp = gameActivityNames[i]) == previousGame);
        myIntent = new Intent(TurnDisplayActivity.this, temp);
        startActivity(myIntent);
    }
}
