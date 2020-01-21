package uoft.csc207.games.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uoft.csc207.games.R;
import uoft.csc207.games.activity.card.CardActivity;
import uoft.csc207.games.controller.ProfileManager;
import uoft.csc207.games.activity.rpg.RpgActivity;
import uoft.csc207.games.activity.dodger.ScrollerActivity;

/**
 * Activity with a single TextView in the center informing the players whose turn it is. Depending on
 * which of the two players just finished playing, TurnDisplayActivity will either go back to the previous
 * game's activity with the second player as the current player, or move on to a different, randomly
 * selected game with the first player as the current player.
 */
public class TurnDisplayActivity extends AppCompatActivity{
    /**
     * Displays which player's turn it is
     */
    private TextView currentTurnDisplay;
    /**
     * Holds the class types of each game's starting activity
     */
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
        //Decides which activity to move to when the player clicks the screen
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            Bundle extras = getIntent().getExtras();
            Intent myIntent;
            //Gets the class name of the previous game class (as a String) the was putExtra'd into the intent by
            //the previous activity
            String sourceClassName = extras.getString("SOURCE_ACTIVITY");
            Class sourceClass = null;
            try{
                //converts the retrieved String to Class type
                sourceClass = Class.forName(sourceClassName);
            } catch (ClassNotFoundException e){
            }

            //Decides whether to go to a random game or repeat the same game based on which turn it is
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

    /**
     * Initializes the List of all games' activities that TurnDisplayActivity can choose to travel to
     */
    private void initClassTypeLists(){
        gameActivities = new ArrayList<>();
        gameActivities.add(RpgActivity.class);
        gameActivities.add(ScrollerActivity.class);
        gameActivities.add(CardActivity.class);
    }

    /**
     * Moves to a random game different from the previously played one
     * @param previousGame The Class type of the previous game's activity
     */
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
