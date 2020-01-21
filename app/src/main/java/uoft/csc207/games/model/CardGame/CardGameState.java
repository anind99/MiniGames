package uoft.csc207.games.model.CardGame;


import android.media.Image;
import android.widget.ImageView;

public class CardGameState {

    private EnemyAI enemyAI; // the enemyAI that will play against the player
    private CardDeck playerDeck, aiDeck; // the cards in the deck
    private int playerHealth, aiHealth;
    private CardCollection aiHand, aiBoard, playerHand, playerBoard; // the cards in the slots
    private boolean[] attacked; // track whether each card has attacked this turn
    private boolean summoned; // track whether the player has summoned this turn
    private boolean firstTurn; // track whether this is the first turn
    private ImageView[] playerHandView; // The View objects on the board
    private ImageView[] playerBoardView;
    private ImageView[] aiBoardView;
    private ImageView[] aiHandView;

    CardGameState(ImageView[] playerHandView, ImageView[] playerBoardView, ImageView[] aiHandView,
                  ImageView[] aiBoardView) {
        int handCap = 3;
        int boardCap = 3;

        enemyAI = new EnemyAI();

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

        attacked = new boolean[boardCap];
        summoned = false;
        firstTurn = true;

        for (int i = 2; i >= 0; i--) {
            attacked[i] = false;
        }
    }

    /**
     * Directly attack target using card, reducing their health by the card's attack
     *
     * @param damage the damage that would be dealt to any of the two parties
     * @param target who should be attacked - assumed to be the player unless "ai" is given
     */
    void attack(int damage, String target) {
        if (target.equals("ai")) {
            // don't let anyone's health go below 0
            this.aiHealth = Math.max(this.aiHealth - damage, 0);
        } else {
            this.playerHealth = Math.max(this.playerHealth - damage, 0);
        }
    }

    void restoreAttack() {
        this.attacked[0] = false;
        this.attacked[1] = false;
        this.attacked[2] = false;
    }


    // everything after this is getters and setters

    // decks

    CardDeck getPlayerDeck() {
        return playerDeck;
    }

    CardDeck getAiDeck() {
        return aiDeck;
    }

    // health

    public int getPlayerHealth() {
        return playerHealth;
    }

    public void setPlayerHealth(int playerHealth) {
        this.playerHealth = playerHealth;
    }

    int getAiHealth() {
        return this.aiHealth;
    }

    void setAiHealth(int aiHealth) { this.aiHealth = aiHealth; }

    // AI hand

    Card getAiHand(int index) {
        return aiHand.getCard(index);
    }

    Card setAiHand(int index, Card c) {
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

    Card getPlayerHand(int index) {
        return playerHand.getCard(index);
    }

    Card setPlayerHand(int index, Card c) {
        return playerHand.setCard(index, c);
    }

    /**
     * Return whether there is a card in the player's hand at index
     *
     * @param index the index to check
     * @return whether or not the slot in the player's hand at index is occupied
     */
    boolean getPlayerHandOccupied(int index) {
        return playerHand.isOccupied(index);
    }

    int getPlayerHandSize() {
        return playerHand.getSize();
    }

    int getAIHandSize() {
        return aiHand.getSize();
    }

    Card popPlayerHand(int index) {
        return playerHand.pop(index);
    }

    Card popAiHand(int index) {
        return aiHand.pop(index);
    }

    // enemyAI

    public EnemyAI getEnemyAI() {
        return enemyAI;
    }


    // boards

    CardCollection getFullAiBoard() { return aiBoard; }

    CardCollection getFullPlayerBoard() { return playerBoard; }

    CardCollection getFullAiHand() { return aiHand; }

    CardCollection getFullPlayerHand() { return playerHand; }

    Card getAiBoard(int index) {
        return aiBoard.getCard(index);
    }

    Boolean getAiBoardOccupied(int index) { return aiBoard.isOccupied(index); }

    Card getPlayerBoard(int index) {
        return playerBoard.getCard(index);
    }

    Card setPlayerBoard(int index, Card c) {
        return playerBoard.setCard(index, c);
    }

    boolean getPlayerBoardOccupied(int index) {
        return playerBoard.isOccupied(index);
    }

    // attacked

    boolean getAttacked(int index) {
        return attacked[index];
    }

    void setAttacked(int index, boolean value) {
        attacked[index] = value;
    }

    // summoned

    void setSummoned(boolean summoned) {
        this.summoned = summoned;
    }

    boolean isSummoned() {
        return summoned;
    }

    // first turn boolean

    boolean isFirstTurn() {
        return firstTurn;
    }

    void setFirstTurn(boolean firstTurn) { this.firstTurn = firstTurn; }
}
