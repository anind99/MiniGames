package uoft.csc207.games.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import uoft.csc207.games.R;
import uoft.csc207.games.controller.ProfileManager;
import uoft.csc207.games.model.PlayerProfile;

public class ProfileActivity extends AppCompatActivity {
    private TextView profileStatsTv;
    private Button exit;
    private PlayerProfile currentProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileStatsTv = findViewById(R.id.playStatsTv);
        exit = findViewById(R.id.exitBtn);
        Intent intent = getIntent();
        currentProfile = (PlayerProfile)intent.getSerializableExtra(ProfileManager.CURRENT_PLAYER);
        profileStatsTv.setText("Score: " + currentProfile.getScore() + "\nCurrency: " + currentProfile.getCurrency()
            + "\nAchievements: \n" + currentProfile.getAchievements());

        ProfileManager profileManager = ProfileManager.getProfileManager(this);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ProfileActivity.this, GameSelectActivity.class);
                startActivity(myIntent);
            }
        });

    }
}
