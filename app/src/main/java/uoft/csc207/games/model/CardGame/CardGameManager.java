package uoft.csc207.games.model.CardGame;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.Random;

import uoft.csc207.games.R;
import uoft.csc207.games.activity.AddScoreActivity;
import uoft.csc207.games.activity.GameSelectActivity;
import uoft.csc207.games.controller.ProfileManager;
import uoft.csc207.games.controller.Score;
import uoft.csc207.games.controller.ScoreBoard;
import uoft.csc207.games.model.IGameID;


public class CardGameManager extends AppCompatActivity implements CardClicker, SpellEffect, TargetChoiceDialog.TargetChoiceDialogListener {

    private CardGameState newGame;
    private ImageView[] playerHand;
    private ImageView[] playerBoard;
    private ImageView[] aiBoard;
    private ImageView[] aiHand;
    private CardGame cardGame;
    private TextView score;
    private View curr_layout;
    private int attackOrigin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_main);
        Intent intent = getIntent();

        //Objects positions set up
        cardGame = (CardGame) ProfileManager.getProfileManager(getApplicationContext()).getCurrentPlayer().containsGame(IGameID.CARD);
        playerHand = new ImageView[3];
        playerBoard = new ImageView[3];
        aiBoard = new ImageView[3];
        aiHand = new ImageView[3];
        playerHand[0] = findViewById(R.id.bleft);
        playerHand[1] = findViewById(R.id.bmid);
        playerHand[2] = findViewById(R.id.bright);
        playerBoard[0] = findViewById(R.id.battle_p_left);
        playerBoard[1] = findViewById(R.id.battle_p_mid);
        playerBoard[2] = findViewById(R.id.battle_p_right);
        aiBoard[0] = findViewById(R.id.battle_ai_left);
        aiBoard[1] = findViewById(R.id.battle_ai_mid);
        aiBoard[2] = findViewById(R.id.battle_ai_right);
        aiHand[0] = findViewById(R.id.uleft);
        aiHand[1] = findViewById(R.id.umid);
        aiHand[2] = findViewById(R.id.uright);
        newGame = new CardGameState(playerHand, playerBoard, aiHand, aiBoard);
        score = findViewById(R.id.score);
        curr_layout = findViewById(R.id.linearLayout);
        final CardPool cardPool = cardGame.getCardPool();
        final CardDeck playerDeck = newGame.getPlayerDeck();
        final EnemyAI enemyAI = newGame.getEnemyAI();
        final CardDeck aiDeck = enemyAI.getAiDeck();
        enemyAI.setUpDeck("Ghost Ogre", cardPool);

        //Sets the deck of the player if none from last session
        String deck_name = intent.getStringExtra("Deck Type");
        if (deck_name.equals("Chosen")) {
            deck_name = cardGame.getCardDeck();
        }
        if (deck_name.equals("Ash")) {
            playerDeck.addThree("Raigeki", cardPool);
            playerDeck.addThree("Ash", cardPool);
            playerDeck.addThree("Ghost Ogre", cardPool);
            cardGame.setCardDeck("Ash");
        } else if (deck_name.equals("G_ogre")) {
            playerDeck.addThree("Ghost Ogre", cardPool);
            playerDeck.addThree("Ash", cardPool);
            cardGame.setCardDeck("G_ogre");
        }

        //Recalling old cardGame
        final Button character = findViewById(R.id.character_change);
        final ImageView character_icon = findViewById(R.id.character_icon);
        final Button background = findViewById(R.id.background);
        String currCharacter = cardGame.getCharacter();
        if (currCharacter.equals("Raegan")) {
            character.setText("Obama Mode");
            character_icon.setImageResource(R.drawable.raegan);
        } else {
            character.setText("Raegan Mode");
            character_icon.setImageResource(R.drawable.obama);
        }
        if (cardGame.getMode().equals("Day")) {
            background.setText("Night");
            curr_layout.setBackgroundColor(Color.WHITE);
        } else {
            background.setText("Day");
            curr_layout.setBackgroundColor(Color.DKGRAY);
        }

        //Day mode and night mode changes
        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If the button is "Night"
                if (background.getText().length() == 5) {
                    background.setText("Day");
                    curr_layout.setBackgroundColor(Color.DKGRAY);
                    cardGame.setMode("Night");
                } else {
                    background.setText("Night");
                    curr_layout.setBackgroundColor(Color.WHITE);
                    cardGame.setMode("Day");
                }
            }
        });

        //Character changes
        character.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (character.getText().length() == 11) {
                    character.setText("Obama Mode");
                    character_icon.setImageResource(R.drawable.raegan);
                    cardGame.setCharacter("Raegan");
                } else {
                    character.setText("Raegan Mode");
                    character_icon.setImageResource(R.drawable.obama);
                    cardGame.setCharacter("Obama");
                }
            }
        });

        // Card summoning
        playerHand[0].setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickActivate(newGame, 0);
                    }
                });

        playerHand[1].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickActivate(newGame, 1);
                    }
                });

        playerHand[2].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickActivate(newGame, 2);
                    }
                });


        // Card attack to another card or direct attack, also checks whether win or not
        playerBoard[0].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickTargetAttack(newGame, 0);
                    }
                });

        playerBoard[1].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickTargetAttack(newGame, 1);
                    }
                });

        playerBoard[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickTargetAttack(newGame, 2);
            }
        });

        // Card replenishment and Phase process
        final Button action = findViewById(R.id.action);
        action.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        action.setText("Next Turn");
                        TextView score = findViewById(R.id.score);
                        score.setText("HIGH SCORE: " + cardGame.getScore());
                        // Determines whether loses
                        int curr_deck_size = newGame.getPlayerDeck().getDeckSize();
                        int hand_occupancy = 0;
                        for (int i = 0; i < newGame.getPlayerHandSize(); i++) {
                            if (newGame.getPlayerHandOccupied(i)) {
                                hand_occupancy++;
                            }
                        }

                        if (curr_deck_size + 1 < hand_occupancy) {
                            Snackbar lose_message =
                                    Snackbar.make(
                                            findViewById(R.id.toolbar), R.string.lose_string,
                                            Snackbar.LENGTH_SHORT);
                            lose_message.show();
                        } else {
                            //AI replenishes its hands
                            for (int i = 0; i < newGame.getAIHandSize(); i++) {
                                if (!newGame.getAiHandOccupied(i)) {
                                    Card next_card = aiDeck.getNextCard();

                                    newGame.setAiHand(i, next_card);
                                    aiHand[i].setImageResource(R.drawable.card_back);
                                    aiDeck.removeNextCard();
                                }
                            }
                            // The ai makes its moves if it's not the first turn
                            if (newGame.isFirstTurn()) {
                                newGame.setFirstTurn(false);
                            } else {
                                if (!newGame.getFullAiBoard().isOccupied(0)) {
                                    if (newGame.getFullAiHand().isOccupied(0)) {
                                        MonsterCard nextCard = (MonsterCard) newGame.popAiHand(0);
                                        newGame.getFullAiBoard().setCard(0, nextCard);
                                        aiBoard[0].setImageResource(nextCard.getCardArt());
                                        newGame.getFullAiHand().setCard(0, CardCollection.emptyCard);
                                    }
                                }
                                for (int i = 0; i < 3; i++) {
                                    aiHand[i].setImageResource(R.drawable.card_back);
                                }
                            }


                            // Replenish cards in hand
                            for (int i = 0; i < newGame.getPlayerHandSize(); i++) {
                                if (!newGame.getPlayerHandOccupied(i)) {
                                    Card next_card = playerDeck.getNextCard();
                                    newGame.setPlayerHand(i, next_card);
                                    playerHand[i].setImageResource(next_card.getCardArt());
                                    playerDeck.removeNextCard();
                                }
                            }
                        }
                        newGame.setSummoned(false);
                        newGame.restoreAttack();
                        cardGame.checkAchievements();
                    }
                });

        final Button exitButton = findViewById(R.id.exitCardGame);
        exitButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cardGame.updateScore(cardGame.getCurrentScore());
                        cardGame.checkAchievements();
                        ProfileManager.getProfileManager(getApplicationContext()).saveProfiles();
                        Intent intent = new Intent(CardGameManager.this,
                                GameSelectActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }

    public void clickSummon(CardGameState cardGameState, int posIndex) {
        if (!cardGameState.isSummoned()) {
            if (!cardGameState.getPlayerHandOccupied(posIndex) ==
                    cardGameState.getPlayerBoardOccupied(posIndex)) {
                MonsterCard next_card = (MonsterCard) cardGameState.popPlayerHand(posIndex);
                cardGameState.setPlayerBoard(posIndex, next_card);
                playerBoard[posIndex].setImageResource(next_card.getCardArt());
                playerHand[posIndex].setImageResource(R.drawable.square);
                cardGameState.setSummoned(true);
            } else if (!(cardGameState.getPlayerHand(posIndex).getCardArt() ==
                    R.drawable.square)) {
                Snackbar cannot_summon =
                        Snackbar.make(findViewById(R.id.toolbar), R.string.slot_occupied,
                                Snackbar.LENGTH_SHORT);
                cannot_summon.show();
            }
        } else {
            Snackbar already_summoned = Snackbar.make(findViewById(R.id.toolbar),
                    R.string.summoned, Snackbar.LENGTH_SHORT);
            already_summoned.show();
        }
    }

    @Override
    public void clickActivate(CardGameState cardGameState, int posIndex) {
        if (cardGameState.getPlayerHand(posIndex) instanceof MonsterCard) {
            clickSummon(cardGameState, posIndex);
        } else {
            SpellCard spell = (SpellCard) cardGameState.popPlayerHand(posIndex);
            String spellEffect = spell.getSpellEffect();
            switch (spellEffect) {
                case "destroyOneRandom":
                    destroyOneRandom();
                    break;
                case "destroyAll":
                    destroyAll();
                    break;
                case "increaseHP":
                    increaseHP(spell.getEffectValue());
                    break;
                case "decreaseHP":
                    decreaseHP(spell.getEffectValue());
                    break;
                case "attackAgain":
                    attackAgain();
                    break;
            }
            playerHand[posIndex].setImageResource(R.drawable.square);
        }
    }

    public void clickDirectAttack(CardGameState cardGameState, int posIndex) {
        if (!cardGameState.getAiHandOccupied(0) & !cardGameState.getAiBoardOccupied(1) &
        !cardGameState.getAiBoardOccupied(0)) {
            int cardAttack = ((MonsterCard) cardGameState.getPlayerBoard(posIndex)).getAttack();
            cardGameState.attack(cardAttack, "ai");
            TextView ai_lp = findViewById(R.id.ai_lp);
            newGame.attack(cardAttack, "ai");
            ai_lp.setText("LP" + newGame.getAiHealth());
        }
        }


    @Override
    public void destroyOneRandom() {
        if (newGame.getFullAiBoard().getOccupiedSize() > 0) {
            Random random = new Random();
            int posNext = random.nextInt();
            newGame.getFullAiBoard().setCard(posNext, CardCollection.emptyCard);
            aiBoard[posNext].setImageResource(R.drawable.square);
            cardGame.updateCurrency(cardGame.getCurrentScore() + 1);
        }

    }

    @Override
    public void destroyAll() {
        for (int i = 0; i < 3; i++) {
            if (!newGame.getAiBoard(i).equals(CardCollection.emptyCard)) {
                cardGame.updateCurrency(cardGame.getGameCurrency() + 1);
            }
            newGame.getFullAiBoard().setCard(i, CardCollection.emptyCard);
            aiBoard[i].setImageResource(R.drawable.square);
        }
    }

    @Override
    public void increaseHP(int healthPoint) {
        TextView player_lp = findViewById(R.id.p_lp);
        newGame.setPlayerHealth(newGame.getPlayerHealth() + healthPoint);
        player_lp.setText("LP" + newGame.getPlayerHealth());

    }

    @Override
    public void decreaseHP(int healthPoint) {
        TextView ai_lp = findViewById(R.id.ai_lp);
        newGame.attack(healthPoint, "ai");
        ai_lp.setText("LP" + newGame.getAiHealth());
    }

    @Override
    public void attackAgain() {
        newGame.restoreAttack();
    }

    @Override
    public void clickTargetAttack(CardGameState cardGameState, int posIndex) {
        if (cardGameState.getPlayerBoardOccupied(posIndex)){
            attackOrigin = posIndex;
            openDialog();
        }
    }


    @Override
    public void clickAttack(CardGameState cardGameState, int posIndex, int targetPosIndex) {
        if (!cardGameState.getAttacked(posIndex)) {
            int damageDifference =
                    ((MonsterCard) cardGameState.getPlayerBoard(posIndex)).getAttack() -
                    ((MonsterCard) cardGameState.getAiBoard(targetPosIndex)).getAttack();
            if (damageDifference > 0) {
                //Destroys ai monster card and deals damage difference to AI
                cardGameState.getFullAiBoard().setCard(targetPosIndex, CardCollection.emptyCard);
                aiBoard[targetPosIndex].setImageResource(R.drawable.square);
                cardGameState.attack(damageDifference, "ai");
                cardGame.updateCurrency(1 + cardGame.getGameCurrency());
            } else if (damageDifference < 0) {
                //Destroys own monster card and deals damage difference to self
                cardGameState.getFullPlayerBoard().setCard(posIndex, CardCollection.emptyCard);
                playerBoard[posIndex].setImageResource(R.drawable.square);
                cardGameState.attack(damageDifference, "player");
            } else {
                //Destroys both monsters
                cardGameState.getFullPlayerBoard().setCard(posIndex, CardCollection.emptyCard);
                aiBoard[targetPosIndex].setImageResource(R.drawable.square);
                cardGameState.getFullAiBoard().setCard(targetPosIndex, CardCollection.emptyCard);
                playerBoard[posIndex].setImageResource(R.drawable.square);
                cardGame.updateCurrency(1 + cardGame.getGameCurrency());
            }
            //Updates Score and LP
            TextView ai_lp = findViewById(R.id.ai_lp);
            TextView player_lp = findViewById(R.id.p_lp);
            ai_lp.setText("LP: " + cardGameState.getAiHealth());
            player_lp.setText("LP" + cardGameState.getPlayerHealth());
            cardGameState.setAttacked(posIndex, true);
            int currentScore = cardGame.getCurrentScore();
            if (!(damageDifference == 0)) {
                cardGame.setCurrentScore(currentScore +
                        ((MonsterCard) cardGameState.getPlayerBoard(posIndex)).getAttack());
            }
            //Announces victory
            if (cardGameState.getAiHealth() == 0) {
                int currScore = cardGame.getCurrentScore();
                cardGame.setCurrentScore(currScore + 3000);
                cardGame.updateScore(cardGame.getCurrentScore());
                cardGame.setCurrentScore(0);
                score.setText("HIGH SCORE: " + cardGame.getScore());
                cardGame.checkAchievements();
                cardGame.setCumulativeCurrency(cardGame.getCumulativeCurrency()+cardGame.getGameCurrency());
                cardGame.setCumulativeScore(cardGame.getCumulativeScore()+cardGame.getScore());
                ScoreBoard.setCurrentScore(new Score("", cardGame.getScore(), cardGame.getGameCurrency(),
                        CardActivity.class.getName()));
                ProfileManager.getProfileManager(getApplicationContext()).saveProfiles();
                Snackbar winner_msg =
                        Snackbar.make(
                                findViewById(R.id.toolbar), R.string.winner_msg, Snackbar.LENGTH_LONG);
                winner_msg.show();
                Intent newIntent = new Intent(CardGameManager.this, AddScoreActivity.class);
                startActivity(newIntent);
            }
        }  else {
            Snackbar attacked =
                    Snackbar.make(findViewById(R.id.toolbar), R.string.attacked,
                            Snackbar.LENGTH_SHORT);
            attacked.show();
        }
    }

    public void openDialog() {
        TargetChoiceDialog targetChoiceDialog = new TargetChoiceDialog();
        targetChoiceDialog.show(getSupportFragmentManager(), "Target Choice");
    }


    @Override
    public void onOtherPlayerClicked() {
        clickDirectAttack(newGame, attackOrigin);
    }

    @Override
    public void onLeftCardClicked() {
        if (newGame.getFullAiBoard().isOccupied(0)) {
            clickAttack(newGame, attackOrigin, 0);
        }
    }

    @Override
    public void onMiddleCardClicked(){
        if (newGame.getFullAiBoard().isOccupied(1)) {
            clickAttack(newGame, attackOrigin, 1);
        }
    }

    @Override
    public void onRightCardClicked() {
        if (newGame.getFullAiBoard().isOccupied(2)) {
            clickAttack(newGame, attackOrigin, 2);
        }
    }
}