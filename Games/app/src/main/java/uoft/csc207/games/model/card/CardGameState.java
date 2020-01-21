package uoft.csc207.games.model.card;


import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import uoft.csc207.games.R;

public class CardGameState implements CardClicker, SpellEffect {

    private CardGame cardGame; // the Game class that is playing this CardGameState
    private EnemyAI enemyAI; // the enemyAI that will play against the player
    private CardDeck playerDeck, aiDeck; // the cards in the deck
    private int playerHealth, aiHealth;
    private CardCollection aiHand, aiBoard, playerHand, playerBoard; // the cards in the slots
    private boolean[] attacked; // track whether each card has attacked this turn
    private boolean summoned; // track whether the player has summoned this turn
    private boolean firstTurn; // track whether this is the first turn
    private TextView aiLP, playerLP; // the View of Life points on board
    private ImageView[] playerHandView; // The View objects on the board
    private ImageView[] playerBoardView;
    private ImageView[] aiBoardView;
    private ImageView[] aiHandView;
    private int attackOrigin; // the attacking card's position on player's board

    public CardGameState(ImageView[] playerHandView, ImageView[] playerBoardView, ImageView[] aiHandView,
                         ImageView[] aiBoardView, CardGame cardGame, TextView aiLP, TextView playerLP) {
        int handCap = 3;
        int boardCap = 3;

        this.cardGame = cardGame;
        this.aiLP = aiLP;
        this.playerLP = playerLP;

        playerDeck = new CardDeck();
        aiDeck = new CardDeck();

        playerHealth = 4000;
        aiHealth = 4000;

        aiHand = new CardCollection(handCap);
        aiBoard = new CardCollection(boardCap);
        playerHand = new CardCollection(handCap);
        playerBoard = new CardCollection(boardCap);

        this.playerHandView = playerHandView;
        this.playerBoardView = playerBoardView;
        this.aiHandView = aiHandView;
        this.aiBoardView = aiBoardView;

        enemyAI = new EnemyAI(aiHandView, aiBoardView);

        attacked = new boolean[boardCap];
        summoned = false;
        firstTurn = true;

        for (int i = 2; i >= 0; i--) {
            attacked[i] = false;
        }
    }

    /**
     * Attack using the damage given to the target indicated, not allowing for health to go below 0
     *
     * @param damage the damage that would be dealt to any of the two parties
     * @param target who should be attacked - assumed to be the player unless "ai" is given
     */
    public void attack(int damage, String target) {
        if (target.equals("ai")) {
            // don't let anyone's health go below 0
            this.aiHealth = Math.max(this.aiHealth - damage, 0);
        } else {
            this.playerHealth = Math.max(this.playerHealth - damage, 0);
        }
    }

    public void restoreAttack() {
        this.attacked[0] = false;
        this.attacked[1] = false;
        this.attacked[2] = false;
    }


    // everything after this is getters and setters

    // decks

    public CardDeck getPlayerDeck() {
        return playerDeck;
    }

    public CardDeck getAiDeck() {
        return aiDeck;
    }

    // health

    public int getPlayerHealth() {
        return playerHealth;
    }

    public void setPlayerHealth(int playerHealth) {
        this.playerHealth = playerHealth;
    }

    public int getAiHealth() {
        return this.aiHealth;
    }

    void setAiHealth(int aiHealth) {
        this.aiHealth = aiHealth;
    }

    // AI hand

    public Card getAiHand(int index) {
        return aiHand.getCard(index);
    }

    public Card setAiHand(int index, Card c) {
        return aiHand.setCard(index, c);
    }

    /**
     * Return whether there is a card in the AI's hand at index
     *
     * @param index the index to check
     * @return whether or not the slot in the AI's hand at index is occupied
     */
    public boolean getAiHandOccupied(int index) {
        return aiHand.isOccupied(index);
    }

    // player hand

    public Card getPlayerHand(int index) {
        return playerHand.getCard(index);
    }

    public Card setPlayerHand(int index, Card c) {
        return playerHand.setCard(index, c);
    }

    /**
     * Return whether there is a card in the player's hand at index
     *
     * @param index the index to check
     * @return whether or not the slot in the player's hand at index is occupied
     */
    public boolean getPlayerHandOccupied(int index) {
        return playerHand.isOccupied(index);
    }

    public boolean getAIHandOccupied(int index) {
        return aiHand.isOccupied(index);
    }

    public int getPlayerHandSize() {
        return playerHand.getSize();
    }

    public int getAIHandSize() {
        return aiHand.getSize();
    }

    public Card popPlayerHand(int index) {
        return playerHand.pop(index);
    }

    public Card popAiHand(int index) {
        return aiHand.pop(index);
    }

    // enemyAI

    public EnemyAI getEnemyAI() {
        return enemyAI;
    }


    // boards

    public Card getAiBoard(int index) {
        return aiBoard.getCard(index);
    }

    public Boolean getAiBoardOccupied(int index) {
        return aiBoard.isOccupied(index);
    }

    public void setAiBoard(int index, Card c) {
        aiBoard.setCard(index, c);
    }

    public Card getPlayerBoard(int index) {
        return playerBoard.getCard(index);
    }

    public Card setPlayerBoard(int index, Card c) {
        return playerBoard.setCard(index, c);
    }

    public boolean getPlayerBoardOccupied(int index) {
        return playerBoard.isOccupied(index);
    }

    // attacked

    public boolean getAttacked(int index) {
        return attacked[index];
    }

    void setAttacked(int index, boolean value) {
        attacked[index] = value;
    }

    // summoned

    public void setSummoned(boolean summoned) {
        this.summoned = summoned;
    }

    public boolean isSummoned() {
        return summoned;
    }

    // first turn boolean

    public boolean isFirstTurn() {
        return firstTurn;
    }

    public void setFirstTurn(boolean firstTurn) {
        this.firstTurn = firstTurn;
    }

    // attack origin getter and setter

    public int getAttackOrigin() {
        return attackOrigin;
    }

    public void setAttackOrigin(int attackOrigin) {
        this.attackOrigin = attackOrigin;
    }

    // CardClicker Methods

    @Override
    /**
     * Take the difference in the monsters' attacks and either destroy the weaker one and inflict
     * the damage difference, or mutual destruction, if an enemy monster is destroyed, its attack is
     * added to the score and the currency goes up by 1
     */
    public void clickAttack(CardGameState cardGameState, int posIndex, int targetPosIndex) {
        int damageDifference =
                ((MonsterCard) getPlayerBoard(posIndex)).getAttack() -
                        ((MonsterCard) getAiBoard(targetPosIndex)).getAttack();
        if (damageDifference > 0) {
            //Destroys ai monster card and deals damage difference to AI
            aiBoard.setCard(targetPosIndex, CardCollection.emptyCard);
            aiBoardView[targetPosIndex].setImageResource(R.drawable.square);
            attack(damageDifference, "ai");
            cardGame.updateCurrency(1 + cardGame.getGameCurrency());
        } else if (damageDifference < 0) {
            //Destroys own monster card and deals damage difference to self
            playerBoard.setCard(posIndex, CardCollection.emptyCard);
            playerBoardView[posIndex].setImageResource(R.drawable.square);
            attack(damageDifference, "player");
        } else {
            //Destroys both monsters
            playerBoard.setCard(posIndex, CardCollection.emptyCard);
            aiBoardView[targetPosIndex].setImageResource(R.drawable.square);
            aiBoard.setCard(targetPosIndex, CardCollection.emptyCard);
            playerBoardView[posIndex].setImageResource(R.drawable.square);
            cardGame.updateCurrency(1 + cardGame.getGameCurrency());
        }
        aiLP.setText("LP: " + cardGameState.getAiHealth());
        playerLP.setText("LP" + cardGameState.getPlayerHealth());
        setAttacked(posIndex, true);
        int currentScore = cardGame.getCurrentScore();
        if (!(damageDifference == 0)) {
            cardGame.setCurrentScore(currentScore +
                    ((MonsterCard) getPlayerBoard(posIndex)).getAttack());
        }
    }

    @Override
    /**
     * Summons a card to its position on the board, then erasing the ImageResource set as its hand
     */
    public void clickSummon(CardGameState cardGameState, int posIndex) {
        MonsterCard next_card = (MonsterCard) popPlayerHand(posIndex);
        setPlayerBoard(posIndex, next_card);
        playerBoardView[posIndex].setImageResource(next_card.getCardArt());
        playerHandView[posIndex].setImageResource(R.drawable.square);
        setSummoned(true);
    }

    @Override
    /**
     * Directly attacks the enemy, then sets their health
     */
    public void clickDirectAttack(CardGameState cardGameState, int posIndex) {
        int cardAttack = ((MonsterCard) getPlayerBoard(posIndex)).getAttack();
        attack(cardAttack, "ai");
        aiLP.setText("LP: " + getAiHealth());
    }

    @Override
    /**
     * Sets the attack origin of the CardGameState object to be the indicated position
     */
    public void clickTargetAttack(CardGameState cardGameState, int posIndex) {
        setAttackOrigin(posIndex);
    }

    @Override
    /**
    Activates a spell effect based on the spellEffect attribute of a SpellCard class object, then
    setting the hand to empty the spell card's card slot
     */
    public void clickActivate(CardGameState cardGameState, int posIndex) {
        SpellCard spell = (SpellCard) popPlayerHand(posIndex);
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
        playerHandView[posIndex].setImageResource(R.drawable.square);
    }

    @Override
    public void destroyAll() {
        for (int i = 0; i < 3; i++) {
            if (!getAiBoard(i).equals(CardCollection.emptyCard)) {
                cardGame.updateCurrency(cardGame.getGameCurrency() + 1);
            }
            aiBoard.setCard(i, CardCollection.emptyCard);
            aiBoardView[i].setImageResource(R.drawable.square);
        }
    }

    @Override
    public void destroyOneRandom() {
        if (aiBoard.getOccupiedSize() > 0) {
            Random random = new Random();
            int posNext = random.nextInt();
            aiBoard.setCard(posNext, CardCollection.emptyCard);
            aiBoardView[posNext].setImageResource(R.drawable.square);
            cardGame.updateCurrency(cardGame.getCurrentScore() + 1);
        }

    }

    @Override
    public void increaseHP(int healthPoint) {
        setPlayerHealth(getPlayerHealth() + healthPoint);
        playerLP.setText("LP: " + getPlayerHealth());

    }

    @Override
    public void decreaseHP(int healthPoint) {
        attack(healthPoint, "ai");
        aiLP.setText("LP: " + getAiHealth());
    }

    @Override
    public void attackAgain() {
        restoreAttack();
    }
}
