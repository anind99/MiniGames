package uoft.csc207.games.model.CardGame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import uoft.csc207.games.R;
import uoft.csc207.games.controller.ProfileManager;
import uoft.csc207.games.model.IGameID;

public class CardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deck_selection);
        Intent intent = getIntent();
        ImageView ash = findViewById(R.id.imageView);
        ImageView g_ogre = findViewById(R.id.imageView2);
        Button lastDeck = findViewById(R.id.last_deck);


        ProfileManager profileManager = ProfileManager.getProfileManager(getApplicationContext());
        if (profileManager.getCurrentPlayer().containsGame(IGameID.CARD) == null) {
            profileManager.getCurrentPlayer().addGame(new CardGame());
        }
        ash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CardActivity.this, CardGameManager.class);
                intent.putExtra("Deck Type", "Ash");
                startActivity(intent);
            }
        });

        g_ogre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CardActivity.this, CardGameManager.class);
                intent.putExtra("Deck Type", "G_ogre");
                startActivity(intent);
            }
        });
        lastDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent oldIntent = new Intent(CardActivity.this, CardGameManager.class);
                oldIntent.putExtra("Deck Type", "Chosen");
                startActivity(oldIntent);
            }
        });
    }
}
