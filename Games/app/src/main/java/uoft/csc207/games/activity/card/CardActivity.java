package uoft.csc207.games.activity.card;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import uoft.csc207.games.R;
import uoft.csc207.games.controller.ProfileManager;
import uoft.csc207.games.model.card.CardGame;
import uoft.csc207.games.model.IGameID;
import uoft.csc207.games.controller.card.CardGameManager;

public class CardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deck_selection);
        Intent intent = getIntent();

        /**
         * Sets the ImagesView to the images and the Button to the lastDeck
         */
        ImageView ash = findViewById(R.id.imageView);
        ImageView g_ogre = findViewById(R.id.imageView2);
        Button lastDeck = findViewById(R.id.last_deck);

        /**
         * This checks whether a CardGame is already within the player's list of games, and if not
         * then add one
         */
        ProfileManager profileManager = ProfileManager.getProfileManager(getApplicationContext());
        if (profileManager.getCurrentPlayer().containsGame(IGameID.CARD) == null) {
            profileManager.getCurrentPlayer().addGame(new CardGame());
        }
        /**
         * This checks whether any of the three are clicked on, and if the images are clicked on,
         * then pass message of their respective deck type to CardGameManager, where it will
         * assemble a deck based off the message. If the button is clicked, then it will pass the
         * message that the old deck would be used to the next activity.
         */
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
