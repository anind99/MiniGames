package uoft.csc207.games.model.rpg;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Iterator;
import java.util.List;

/**
 * Represents a static image that has its own dialogue options when interacted with and occupies a
 * space on the screen.
 */
public class NpcCharacter extends GameObject implements Obstructable {
    public static final int UP_ROW = 0;
    public static final int LEFT_ROW = 1;
    public static final int DOWN_ROW = 2;
    public static final int RIGHT_ROW = 3;

    /**
     * The column is 0 because the sprite sheet's static image is in the first column
     */
    private static int COL_USING = 0;
    private int rowUsing;

    private Bitmap up;      //the image of the character facing up
    private Bitmap down;    //the image of the character facing down
    private Bitmap left;    //the image of the character facing left
    private Bitmap right;   //the image of the character facing right

    /**
     * Used to differentiate whether it is the NPC's first interaction in the character
     */
    private boolean talkedToAlready = false;
    /**
     * Represents whether a given obstruction is the very first point of contact with the PlayerCharacter,
     * and not whether it is the NPC's first time obstructing the player
     */
    private boolean isFirstObstruction = true;
    /**
     * The set of dialogue lines the NPC goes through when the PlayerCharacter first talks to them
     */
    private List<String> dialogue;
    /**
     * The line the NPC repeats after the first encounter
     */
    private String afterTalkedToText;
    private Iterator<String> dialogueIterator;

    /**
     * Creates an npc at a given position with the appropriate text and appearance.
     * @param image The sprite sheet for the npc
     * @param x The npc's x position
     * @param y The npc's y position
     * @param dialogue The npc's dialogue
     * @param talkedToText The repeated text after the first encounter
     * @param direction The initial facing direction
     */
    public NpcCharacter(Bitmap image, int x, int y, List<String> dialogue, String talkedToText, int direction){
        super(image, 21, 13, x, y);
        rowUsing = direction;

        this.up = this.createSubImageAt(UP_ROW, COL_USING);
        this.left = this.createSubImageAt(LEFT_ROW, COL_USING);
        this.down = this.createSubImageAt(DOWN_ROW, COL_USING);
        this.right = this.createSubImageAt(RIGHT_ROW, COL_USING);

        this.dialogue = dialogue;
        afterTalkedToText = talkedToText;
        dialogueIterator = dialogue.iterator();
    }

    public boolean hasTalkedToAlready(){
        return talkedToAlready;
    }

    public void setTalkedToAlready(boolean b){
        talkedToAlready = b;
    }

    public boolean isFirstObstruction(){ return isFirstObstruction; }

    public void setFirstObstruction(boolean b){ isFirstObstruction = b; }

    public String getAfterTalkedToText(){
        return afterTalkedToText;
    }

    /**
     * @return The Bitmap of the direction the npc is facing.
     */
    public Bitmap getCurrentBitmap(){
        Bitmap result = null;
        switch (rowUsing){
            case UP_ROW:
                result = up;
                break;
            case LEFT_ROW:
                result = left;
                break;
            case DOWN_ROW:
                result = down;
                break;
            case RIGHT_ROW:
                result = right;
                break;
        }
        return result;
    }

    public void update(){
        /*
            For future implementation where NPC turns to face the character
         */
    }

    /**
     * @return Whether the npc has remaining dialogue lines to say
     */
    public boolean hasNextDialogue(){
        return dialogueIterator.hasNext();
    }

    /**
     * @return The next line of dialogue.
     */
    public String getNextDialogue(){
        String result = null;
        if (dialogueIterator.hasNext()){
            result = dialogueIterator.next();

        }
        return result;
    }

    /**
     * Draws itself on a specific position on the cavas. Offset so that the Npc's co-ordinates are at
     * the center of the image.
     * @param canvas The Canvas the npc draws itself on
     */
    public void draw(Canvas canvas){
        canvas.drawBitmap(getCurrentBitmap(), x - (singleWidth / 2), y - (singleWidth / 2), null);
    }
}
