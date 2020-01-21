package uoft.csc207.games.model.Rpg;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NpcCharacter extends GameObject implements Obstructable {
    public static final int UP_ROW = 0;
    public static final int LEFT_ROW = 1;
    public static final int DOWN_ROW = 2;
    public static final int RIGHT_ROW = 3;

    private static int COL_USING = 0;
    private int rowUsing;

    private Bitmap up;
    private Bitmap down;
    private Bitmap left;
    private Bitmap right;

    /**
     *
     */
    private boolean talkedToAlready = false;
    private boolean isFirstObstruction = true;
    private List<String> dialogue;
    private String afterTalkedToText;
    private Iterator<String> dialogueIterator;

    private GameSurface gameSurface;

    public NpcCharacter(Bitmap image, int x, int y, List<String> dialogue, String talkedToText, int direction){
        super(image, 21, 13, x, y);
        //rowUsing = LEFT_ROW;    //for now, will be decided by a parameter later
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

    public List<String> getDialogue(){
        return dialogue;
    }

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
    public boolean hasNextDialogue(){
        return dialogueIterator.hasNext();
    }
    public String getNextDialogue(){
        String result = null;
        if (dialogueIterator.hasNext()){
            result = dialogueIterator.next();

        }
        return result;
    }

    public void draw(Canvas canvas){
        //Bitmap bitmap = left;
        canvas.drawBitmap(getCurrentBitmap(), x - (singleWidth / 2), y - (singleWidth / 2), null);
    }
}
