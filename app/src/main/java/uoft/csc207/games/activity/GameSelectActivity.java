package uoft.csc207.games.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import uoft.csc207.games.model.CardGame.CardActivity;
import uoft.csc207.games.model.PlayerProfile;
import uoft.csc207.games.controller.ProfileManager;
import uoft.csc207.games.R;
import uoft.csc207.games.activity.rpg.RpgActivity;
import uoft.csc207.games.activity.dodger.ScrollerActivity;
import uoft.csc207.games.model.dodger.Constants;

public class GameSelectActivity extends AppCompatActivity {
    private TextView welcome;
    private Button logout;
    private PlayerProfile currentProfile;

    private Button scrollerSelect;
    private Button cardSelect;
    private Button rpgSelect;
    private Button profileButton;
    private Button leaderBoard;

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
        if (profileManager.isTwoPlayerMode()){
            welcome.setText("Welcome back " + currentProfile.getId() + " and " + profileManager.
                    getSecondPlayer().getId() + "!");
        } else {
            welcome.setText("Welcome back " + currentProfile.getId() + "!");
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileManager.getProfileManager(getApplicationContext()).saveProfiles();
                moveToLoginActivity();
            }
        });

        leaderBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileManager.getProfileManager(getApplicationContext()).saveProfiles();
                moveToScoreActivity();
            }
        });

        scrollerSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sIntent = new Intent(GameSelectActivity.this, ScrollerActivity.class);
                startActivity(sIntent);
            }
        });

        cardSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (!(currentProfile.containsGame(IGameID.CARD) instanceof CardGame)) {
                    currentProfile.addGame(new CardGame());
                }*/
                Intent cardIntent = new Intent(GameSelectActivity.this, CardActivity.class);
                startActivity(cardIntent);
            }
        });
        /**
         * Button that transfers to RPG game
         */
        rpgSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(GameSelectActivity.this, RpgActivity.class);
                startActivity(myIntent);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(GameSelectActivity.this, ProfileActivity.class);
                myIntent.putExtra(ProfileManager.CURRENT_PLAYER, currentProfile);
                startActivity(myIntent);
            }
        });


    }

    private void moveToLoginActivity(){
        Intent intent = new Intent(GameSelectActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void moveToScoreActivity(){
        Intent intent = new Intent(GameSelectActivity.this, LeaderBoardActivity.class);
        startActivity(intent);
    }
}
