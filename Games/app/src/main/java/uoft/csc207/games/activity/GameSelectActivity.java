package uoft.csc207.games.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import uoft.csc207.games.activity.card.CardActivity;
import uoft.csc207.games.model.PlayerProfile;
import uoft.csc207.games.controller.ProfileManager;
import uoft.csc207.games.R;
import uoft.csc207.games.activity.rpg.RpgActivity;
import uoft.csc207.games.activity.dodger.ScrollerActivity;
import uoft.csc207.games.model.dodger.Constants;

/**
 * Provides an interface that allows the user to choose one of the three
 * games, view the leader board, view their statistics across all the games, or log out.
 */
public class GameSelectActivity extends AppCompatActivity {
    private TextView welcome;
    private Button logout;
    private PlayerProfile currentProfile;

    private Button scrollerSelect;
    private Button cardSelect;
    private Button rpgSelect;
    private Button profileButton;
    private Button leaderBoard;

    /**
     * Initializes all the necessary widgets and their functions.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_select);
        welcome = findViewById(R.id.tvWelcome);
        logout = findViewById(R.id.btnLogout);

        scrollerSelect = findViewById(R.id.btnScroller);
        cardSelect = findViewById(R.id.btnCard);
        rpgSelect = findViewById(R.id.btnRPG);
        profileButton = findViewById(R.id.profileBtn);
        leaderBoard = findViewById(R.id.LeaderBoard);
        Constants.CURRENT_CONTEXT = this;

        ProfileManager profileManager = ProfileManager.getProfileManager(getApplicationContext());
        currentProfile = profileManager.getCurrentPlayer();
        //Provides a welcome message at the top based off how many users are logged in
        if (profileManager.isTwoPlayerMode()){
            welcome.setText("Welcome back " + currentProfile.getId() + " and " + profileManager.
                    getSecondPlayer().getId() + "!");
        } else {
            welcome.setText("Welcome back " + currentProfile.getId() + "!");
        }
        //Logs the player out on click, moving back to LoginActivity
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileManager.getProfileManager(getApplicationContext()).saveProfiles();
                moveToLoginActivity();
            }
        });
        //Moves to the leader board on click
        leaderBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileManager.getProfileManager(getApplicationContext()).saveProfiles();
                moveToScoreActivity();
            }
        });
        //Moves to the scroller game on click
        scrollerSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sIntent = new Intent(GameSelectActivity.this, ScrollerActivity.class);
                startActivity(sIntent);
            }
        });
        //Moves to the card game on click
        cardSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cardIntent = new Intent(GameSelectActivity.this, CardActivity.class);
                startActivity(cardIntent);
            }
        });
        //Moves to the rpg game on click
        rpgSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(GameSelectActivity.this, RpgActivity.class);
                startActivity(myIntent);
            }
        });
        //Moves to a screen that shows the statistics of all logged in players
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(GameSelectActivity.this, ProfileActivity.class);
                myIntent.putExtra(ProfileManager.CURRENT_PLAYER, currentProfile);
                startActivity(myIntent);
            }
        });


    }

    /**
     * Moves from GameSelect back to the login screen
     */
    private void moveToLoginActivity(){
        Intent intent = new Intent(GameSelectActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * Moves from GameSelect to the leader board screen
     */
    private void moveToScoreActivity(){
        Intent intent = new Intent(GameSelectActivity.this, LeaderBoardActivity.class);
        startActivity(intent);
    }
}
