package uoft.csc207.games.controller.rpg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.TreeMap;

import uoft.csc207.games.R;
import uoft.csc207.games.controller.ProfileManager;
import uoft.csc207.games.model.IGameID;
import uoft.csc207.games.model.PlayerProfile;
import uoft.csc207.games.model.rpg.GameObject;
import uoft.csc207.games.model.rpg.NpcCharacter;
import uoft.csc207.games.model.rpg.Obstructable;
import uoft.csc207.games.model.rpg.PlayerCharacter;
import uoft.csc207.games.activity.rpg.RpgActivity;
import uoft.csc207.games.model.rpg.RpgGameState;

/**
 * This Class is a main class to manage RPG Game's characters (objects of PlayerCharacter,
 * RpgCharacter), plyaer's profile (object of PlayerProfile) and refresh a GameSurface with
 * updated information
 */
public class RPGGameManager {
    private PlayerCharacter playerCharacter;
    /**
     * Each "screen" is a List of GameObjects and changing which list is the currentScreen being drawn
     * and interacted with gives the impression of changing screens
     */
    private List<List<GameObject>> screens = new ArrayList<>();
    private ListIterator<List<GameObject>> screenIterator;
    /**
     * The GameObject list representing the current GameObjects being drawn and used for determining
     * gameplay interactions.
     */
    private List<GameObject> currentScreen;
    private RpgGameState currentGameState;
    private Context currentContext;
    private PlayerProfile currentPlayer;
    /**
     * Paint used for the score and gold text in the top left
     */
    private Paint scorePaint = new Paint();
    /**
     * Paint used for the dialogue text
     */
    private Paint dialoguePaint = new Paint();
    private Paint outerDialogueRectanglePaint = new Paint();
    private Paint innerDialogueRectanglePaint = new Paint();
    private Rect outerRectangle = new Rect();
    private Rect innerRectangle = new Rect();
    /**
     * Represents the current character sprite sheet. Starts as the male.
     */
    private int usingCharacter = R.drawable.c1_sprite_sheet;
    private int hoodedNPCSprites = R.drawable.hooded_npc_sprites;
    private int background = R.drawable.forest_background;
    private int forestPath = R.drawable.forest_path;
    private Bitmap backgroundBitmap;
    private Bitmap forestPathBitMap;
    private TreeMap<String, Integer> characterMap;     //map of character strings to their sprite sheet
    /**
     * Whether the PlayerCharacter is interacting with the npc, in which case mouse clicks will cycle
     * through the npc's dialogue options rather than move the PlayerCharacter.
     */
    private boolean isProcessingText = false;
    /**
     * The last npc the PlayerCharacater has talked to. Used to tell the npc it has been talked to after
     * the PlayerCharacter finishes talking to it and walks away, so the next time they are interacted
     * with it will be the afterTalkedTo text.
     */
    private NpcCharacter lastTalkedToNpc;
    /**
     * Distance between outer and inner rectangles making up the text box
     */
    private final static int OUTER_TO_INNER_OFFSET = 15;
    /**
     * Distance between outer rectangle borders to the text
     */
    private final static int OUTER_TO_TEXT_OFFSET = 30;
    /**
     * Represents all the space below the game space, which is occupied by the dialogue box
     */
    private final static int TEXT_BOX_HEIGHT = 400;
    /**
     * Represents the size of the game space in the y direction (x direction spans the screen width)
     */
    private final static int GAME_SPACE = 500;
    /**
     * Represents all the space above the game space in the center
     */
    private static int backgroundSpace;
    private int canvasWidth;
    private int canvasHeight;
    /**
     * The text currently being displayed in the dialogue box
     */
    private String currentText = "";

    public boolean isProcessingText(){
        return isProcessingText;
    }
    public Paint getScorePaint() {
        return scorePaint;
    }
    /**
     * A constructor of RPGGameManager class
     * @param currentContext a <class>Context</class> object for RPGGameManager retrieve system resource
     */
    public RPGGameManager(Context currentContext, int width, int height) {
        this.currentContext = currentContext;
        playerCharacter = new PlayerCharacter(BitmapFactory.decodeResource(currentContext.getResources(),
                usingCharacter), 200, 1200);
        canvasWidth = width;
        canvasHeight = height;
        backgroundSpace = canvasHeight - TEXT_BOX_HEIGHT - GAME_SPACE;
        initializeGameObjects();
        screenIterator = screens.listIterator();
        currentScreen = screenIterator.next();
        currentPlayer = ProfileManager.getProfileManager(currentContext).getCurrentPlayer();
        currentGameState = new RpgGameState();
    }

    public PlayerCharacter getPlayerCharacter() {
        return playerCharacter;
    }

    public List<GameObject> getCurrentScreen(){
        return currentScreen;
    }

    public RpgGameState getCurrentGameState() {
        return currentGameState;
    }

    /**
     * Initialize all required resources for RPGGame to start up
     */
    public void initialize(){
        initializeGameState();
        initializePaints();
        initializeCharacterMap();
        initializeBackground();
    }

    /**
     * Initializes all the game objects
     */
    public void initializeGameObjects(){
        initializeScreenOne();
        initializeScreenTwo();
    }

    /**
     * Initializes all game objects in the list representing the first "screen"
     */
    private void initializeScreenOne(){
        List<GameObject> screen1 = new ArrayList<>();
        List<String> dialogue1 = new ArrayList<>();
        dialogue1.add("Hey you, you're finally awake.");
        dialogue1.add("...");
        dialogue1.add("Where are you?");
        dialogue1.add("You'll find out soon enough.");
        String talkedToText = "...";
        NpcCharacter npc1 = new NpcCharacter(BitmapFactory.decodeResource(currentContext.getResources(), hoodedNPCSprites),
                500, 1200, dialogue1, talkedToText, NpcCharacter.LEFT_ROW);
        screen1.add(npc1);
        screen1.add(playerCharacter);
        screens.add(screen1);
    }

    /**
     * Initializes all GameObjects in the List representing the "second screen"
     */
    private void initializeScreenTwo(){
        List<GameObject> screen2 = new ArrayList<>();
        List<String> dialogue2 = new ArrayList<>();
        dialogue2.add("A new face? It's been a long time.");
        dialogue2.add("It's better that you leave this place.");
        dialogue2.add("For your sake. And for ours.");
        String talkedToText = "You don't have much time left.";
        NpcCharacter npc2 = new NpcCharacter(BitmapFactory.decodeResource(currentContext.getResources(), hoodedNPCSprites),
                700, 1200, dialogue2, talkedToText, NpcCharacter.DOWN_ROW);
        screen2.add(npc2);
        List<String> dialogue3 = new ArrayList<>();
        dialogue3.add("Don't talk to me.");
        talkedToText = "...";
        NpcCharacter npc3 = new NpcCharacter(BitmapFactory.decodeResource(currentContext.getResources(), hoodedNPCSprites),
                400, backgroundSpace + 300, dialogue3, talkedToText, NpcCharacter.RIGHT_ROW);
        screen2.add(npc3);
        screen2.add(playerCharacter);
        screens.add(screen2);
    }

    /**
     * Creates a RpgGameState if the current player doesn't already have one
     */
    private void initializeGameState(){
        currentGameState = (RpgGameState)currentPlayer.containsGame(IGameID.RPG);
        if(currentGameState == null){
            currentGameState = new RpgGameState();
            currentPlayer.addGame(currentGameState);
        }
    }

    /**
     * Initializes scorePaint which is used for drawing the score and gold, and initializes the paints
     * for the 2 rectangles making up the dialogue box at the bottom.
     */
    private void initializePaints(){
        initializeScorePaint();
        initializeDialoguePaint();
        initializeTextRectanglePaints();
    }

    /**
     * Customizes the score paint based off of the customization options in th RpgGameState object
     */
    private void initializeScorePaint(){
        initializeScorePaintColor();
        initializeScorePaintFont();
        scorePaint.setTextSize(50);
        scorePaint.setAntiAlias(true);
    }

    /**
     * Configures the Score paint's color depending on the information in the RpgGameSate
     */
    private void initializeScorePaintColor(){
        //configure scorePaint
        if(currentGameState.getColor() != null){
            if(currentGameState.getColor().equals(RpgGameState.FONT_COLOR_RED)){
                scorePaint.setColor(Color.RED);
            }else if (currentGameState.getColor().equals(RpgGameState.FONT_COLOR_BLACK)){
                scorePaint.setColor(Color.BLACK);
            }else {
                scorePaint.setColor(Color.WHITE);
            }
        }else {
            scorePaint.setColor(Color.WHITE);
        }
    }

    /**
     * Configures the score paint's font depending on the information in RpgGameState
     */
    private void initializeScorePaintFont(){
        if(currentGameState.getFont() != null){
            if(currentGameState.getFont().equals(RpgGameState.FONT_TYPE_MONOSPACE)){
                scorePaint.setTypeface(Typeface.MONOSPACE);
            }else if (currentGameState.getFont().equals(RpgGameState.FONT_TYPE_SANS_SERIF)){
                scorePaint.setTypeface(Typeface.SANS_SERIF);
            }else {
                scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
            }
        }else{
            scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        }
    }

    /**
     * Initializes the paint used for displaying dialogue
     */
    private void initializeDialoguePaint(){
        dialoguePaint.setColor(Color.WHITE);
        dialoguePaint.setTextSize(60);
        dialoguePaint.setTypeface(Typeface.DEFAULT_BOLD);
        dialoguePaint.setAntiAlias(true);
        dialoguePaint.setTextAlign(Paint.Align.LEFT);
    }

    /**
     * Initializes the paints for the two rectangles making up the dialogue box at the bottom of the screen
     */
    private void initializeTextRectanglePaints(){
        //Creating the text box border
        outerRectangle.set(0,canvasHeight - TEXT_BOX_HEIGHT, canvasWidth, canvasHeight);
        outerDialogueRectanglePaint.setColor(Color.LTGRAY);
        outerDialogueRectanglePaint.setStyle(Paint.Style.FILL);

        //Creating the actual text box
        innerRectangle.set(OUTER_TO_INNER_OFFSET, canvasHeight - TEXT_BOX_HEIGHT + OUTER_TO_INNER_OFFSET,
                canvasWidth - OUTER_TO_INNER_OFFSET, canvasHeight - OUTER_TO_INNER_OFFSET);
        innerDialogueRectanglePaint.setColor(Color.BLACK);
        innerDialogueRectanglePaint.setStyle(Paint.Style.FILL);
    }

    /**
     * Initializes the TreeMap with the addresses for all the PlayerCharacter sprite sheets
     */
    private void initializeCharacterMap(){
        characterMap = new TreeMap<>();
        characterMap.put("male", R.drawable.c1_sprite_sheet);
        characterMap.put("female", R.drawable.c2_sprite_sheet);
    }

    /**
     * Initializes the background bitmaps
     */
    private void initializeBackground(){
        //initializes forest background
        backgroundBitmap = BitmapFactory.decodeResource(currentContext.getResources(), background);
        backgroundBitmap = Bitmap.createBitmap(backgroundBitmap, 0, 872, canvasWidth,
                backgroundSpace);
        //initializes the ground background
        forestPathBitMap = BitmapFactory.decodeResource(currentContext.getResources(), forestPath);
        forestPathBitMap = Bitmap.createBitmap(forestPathBitMap, 1000,
                3300 - backgroundSpace, canvasWidth, GAME_SPACE + backgroundSpace);
    }

    /**
     * Sets the PlayerCharacter's movement vectors and destination coordinates so that the PlayerCharacter
     * will incrementally move towards the new destination on each thread update.
     * @param x The destination x value
     * @param y The destination y value
     */
    public void setPlayerCharacterDestination(int x, int y){
        int movementVectorX = x - playerCharacter.getX();
        int movementVectorY = y - playerCharacter.getY();
        /*
            First checks if PlayerCharacter is not blocked in any direction, and then goes through
            the y direction and x direction cases. Checks if the direction associated with the attempted
            movement is blocked)
         */
        if(!playerCharacter.isBotBlocked() && !playerCharacter.isTopBlocked() && !playerCharacter.isRightBlocked()
                && !playerCharacter.isLeftBlocked()){
            playerCharacter.setMovementVector(movementVectorX, movementVectorY);
            playerCharacter.setDestinationCoordinates(x, y);
        } else if ((!playerCharacter.isLeftBlocked() && movementVectorX < 0) || (!playerCharacter.isRightBlocked() &&
                movementVectorX > 0)){
            playerCharacter.setMovementVector(movementVectorX, playerCharacter.getMovingVectorY());
            playerCharacter.setDestinationCoordinates(x, playerCharacter.getY());
        } else if ((!playerCharacter.isBotBlocked() && movementVectorY > 0) || (!playerCharacter.isTopBlocked() &&
                movementVectorY < 0)){
            playerCharacter.setMovementVector(playerCharacter.getMovingVectorX(), movementVectorY);
            playerCharacter.setDestinationCoordinates(playerCharacter.getX(), y);
        }
    }

    /**
     * Updates all the relevant game information based on things like the PlayerCharacter's current position
     * and the player's current statistics (in currentGameState)
     */
    public void update(){
        playerCharacter.update();
        moveScreensIfPossible();
        checkAndHandleEnvironmentInteractions();
        //Periodically updates the currency when the player is in the game
        if(Math.random() < 0.05){
            currentGameState.updateCurrency(1);
        }
        currentGameState.checkAchievements();
    }

    /**
     * Checks if a PlayerCharacter is close enough to the left and right borders of the screen, and if
     * it is, change the current GameObject list representing the current screen to the appropriate next
     * one.
     */
    private void moveScreensIfPossible(){
        if (playerCharacter.getX() < (playerCharacter.getWidth() / 2)){
            if (screenIterator.hasPrevious()){
                /**
                 * ^ screen1 ^ screen2 ^        (^ = cursor position)
                 * Calling previous() after calling next() will repeat the same screen and move cursor
                 * to the right of the actual previous screen. Call previous() and then call next() to get the
                 * correct screen and position the cursor back to the right of your current element
                 * so the following next() is correct
                 */
                currentScreen = screenIterator.previous();
                currentScreen = screenIterator.previous();
                currentScreen = screenIterator.next();
                playerCharacter.setCoordinates(canvasWidth - playerCharacter.getWidth(), playerCharacter.getY());
                playerCharacter.stopMoving();
            }
        }
        if (playerCharacter.getX() > canvasWidth - (playerCharacter.getWidth() / 2)){
            if (screenIterator.hasNext()){
                currentScreen = screenIterator.next();
                playerCharacter.setCoordinates(playerCharacter.getWidth(), playerCharacter.getY());
                playerCharacter.stopMoving();
            } else {
                ((RpgActivity) currentContext).finishGame(currentGameState, true);
            }
        }
    }

    /**
     * Handles PlayerCharacter's interactions with other GameObjects. Designates which directions the
     * PlayerCharacter is blocked from moving in, and handles Npc interactions accordingly (depending on
     * if you've already talked to them or have talked to them already)
     */
    private void checkAndHandleEnvironmentInteractions(){
        GameObject interceptor = this.findInterceptor();
        if (interceptor != null){
            for(String s: playerCharacter.getBlockedDirections()){
                if (playerCharacter.getMovingDirection().equals(s)){
                    playerCharacter.stopMoving();
                    break;
                }
            }
            //update score
            if (interceptor instanceof NpcCharacter){
                handleNpcInteraction((NpcCharacter) interceptor);
            }
        } else {
            //If you move away from an NPC you were talking to, sets talkedToAlready to true so the
            //next time you talk to them it will always be the repeat text
            if(lastTalkedToNpc != null){
                lastTalkedToNpc.setTalkedToAlready(true);
                lastTalkedToNpc.setFirstObstruction(true);
            }
            playerCharacter.unblockAllDirections();
            currentText = "";
        }
    }

    /**
     * Called when the PlayerCharacter is intercepted by an Npc. Depending on whether the PlayerCharacter
     * has already talked to the npc, chooses the dialogue and updates the score accordingly.
     * @param interceptor The npc that the PlayerCharacter is currently interacting with
     */
    private void handleNpcInteraction(NpcCharacter interceptor){
        NpcCharacter npcInterceptor = interceptor;
        lastTalkedToNpc = npcInterceptor;
        isProcessingText = true;
        if (npcInterceptor.hasTalkedToAlready()){
            currentText = npcInterceptor.getAfterTalkedToText();
            isProcessingText = false;
        } else {
            if(npcInterceptor.hasNextDialogue()){
                currentGameState.updateScore(50);
                currentText = npcInterceptor.getNextDialogue();
            } else {
                isProcessingText = false;
            }
        }
    }

    /**
     * Draws all relevant game assets including backgrounds, the dialogue box, the score/gold, the PlayerCharacter,
     * and all the GameObject objects.
     * @param canvas The Canvas for RPGGameManager to draw on
     */
    public void draw(Canvas canvas){
        canvas.drawBitmap(forestPathBitMap, 0, 0, null);
        /*sorts every time because the PlayerCharacter can change. Sorted by their y coordinate so that
         *GameObjects are drawn in the right order (ex. if PlayerCharacter is behind something, as in higher
         *on the screen, it should be drawn first)
         */
        Collections.sort(currentScreen);
        for(GameObject gameObject: this.currentScreen){
            (gameObject).draw(canvas);
        }
        canvas.drawRect(outerRectangle, outerDialogueRectanglePaint);
        canvas.drawRect(innerRectangle, innerDialogueRectanglePaint);
        /*
            ((RpgActivity) currentContext).finishGame(0);
        */
        //draws the dialogue
        int x = OUTER_TO_TEXT_OFFSET; //canvas.getWidth() - bounds.width();
        int y = canvasHeight - TEXT_BOX_HEIGHT + (OUTER_TO_TEXT_OFFSET * 3); //canvas.getHeight() - bounds.height();
        canvas.drawText(currentText, x, y, dialoguePaint);

        //draws the score and gold display
        String score = "Score: " + currentGameState.getScore();
        String gold = "Gold:  " + currentGameState.getGameCurrency();
        canvas.drawText(score, 40, 50, scorePaint);
        canvas.drawText(gold, 40, 130, scorePaint);
        handleCustomization();
    }

    /**
     * Handles the character customization by setting the appropriate Bitmap sprite sheet for the
     * PlayerCharacter
     */
    private void handleCustomization(){
        String gameStateCharacter = currentGameState.getCharacter();
        if(gameStateCharacter == null || gameStateCharacter.equalsIgnoreCase("male") ){
            usingCharacter = characterMap.get("male");
        } else if (gameStateCharacter.equalsIgnoreCase("female") ){
            usingCharacter = characterMap.get("female");
        }
        Bitmap currentBitmap = BitmapFactory.decodeResource(currentContext.getResources(), usingCharacter);
        playerCharacter.setWalkCycleImages(currentBitmap) ;
    }

    /**
     * Checks if the PlayerCharacter object is intercepted by any GameObject
     * @return The GameObject the PlayerCharacter is intercepted with
     */
    public GameObject findInterceptor(){
        GameObject interceptor = null;
        for (GameObject gameObject: currentScreen)
            if (gameObject instanceof Obstructable){
                if ((Math.abs(gameObject.getX() - playerCharacter.getX()) < (gameObject.getWidth() / 2)) &&
                        (Math.abs(gameObject.getY() - playerCharacter.getY()) < (gameObject.getHeight() / 2)) &&
                        interceptor == null) {
                    interceptor = gameObject;

                    Obstructable o = (Obstructable) gameObject;
                    if (o.isFirstObstruction()) {
                        blockCurrentMovingDirection();
                        o.setFirstObstruction(false);
                    }
                }
            }
        return interceptor;
    }

    /**
     * Blocks the direction the player is currently moving in
     */
    public void blockCurrentMovingDirection(){
        boolean left = playerCharacter.getMovingVectorX() < 0;
        boolean right = playerCharacter.getMovingVectorX() > 0;
        boolean top = playerCharacter.getMovingVectorY() < 0;
        boolean bot = playerCharacter.getMovingVectorY() > 0;
        //because the PlayerCharacter handles movement in the x direction first, if the x vector
        //isn't 0, playerCharacter is moving in the x direction
        if(playerCharacter.getMovingVectorX() != 0){
            if (left){
                playerCharacter.setLeftBlocked(true);
            } else if (right) {
                playerCharacter.setRightBlocked(true);
            }
        } else {
            if(bot){
               playerCharacter.setBotBlocked(true);
            } else if (top){
               playerCharacter.setTopBlocked(true);
           }
       }
    }

    /**
     * Checks if a given yCoordinate is within the game space allocated for player movement
     * @param yCoordinate The y value to evaluate
     * @return Whether the given y value is in the game space
     */
    public boolean isInGameSpace(int yCoordinate){
        return yCoordinate >= (backgroundSpace - (playerCharacter.getHeight() / 2)) && yCoordinate <=
                (canvasHeight - TEXT_BOX_HEIGHT - (playerCharacter.getHeight() / 2));
    }
}
