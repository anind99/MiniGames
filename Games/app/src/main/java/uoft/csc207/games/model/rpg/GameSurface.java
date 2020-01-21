package uoft.csc207.games.model.rpg;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import uoft.csc207.games.controller.rpg.RPGGameManager;

/**
 * The view of the rpg game. GameSurface contains a canvas which all of the GameObjects will be drawn on.
 */
public class GameSurface extends SurfaceView implements SurfaceHolder.Callback{
    /**
     * Used to control updates to the GameSurface.
     */
    private GameThread gameThread;
    /**
     * Handles most of the game play logic and decisions
     */
    private RPGGameManager rpgGameManager;

    /**
     * Creates a SurfaceView as well as an RpgGameManager object which is what's used to decide game
     * play interactions.
     * @param context The current context of the app
     */
    public GameSurface(Context context){
        super(context);
        getHolder().addCallback(this);
        gameThread = new GameThread(this, getHolder());
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;
        rpgGameManager = new RPGGameManager(context, width, height);
        setFocusable(true);
    }

    public RPGGameManager getRpgGameManager() {
        return rpgGameManager;
    }

    public GameThread getGameThread(){
        return gameThread;
    }

    /**
     * Allows updates if the rpgGameManager isn't currently processing text (talking with an npc). Otherwise
     * until the player exhausts all of the npc's dialogue by clicking the screen, there will be no
     * other updates to the game.
     */
    public void update(){
        if (!rpgGameManager.isProcessingText()){
            rpgGameManager.update();
        }
    }

    /**
     * Moves the PlayerCharacter to a given position or moves to the next Npc dialogue depending on
     * whether or not they are currently interacting with an npc
     * @param event The given MotionEvent
     * @return Whether the MotionEvent is a mouse click
     */
    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (rpgGameManager.isProcessingText()){
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                rpgGameManager.update();
            }
        } else {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                int x = (int) event.getX(); //- (rpgGameManager.getPlayerCharacter().getWidth() / 2);
                int y = (int) event.getY(); //- (rpgGameManager.getPlayerCharacter().getHeight() / 2);
                //if the attempted movement is within bounds, allow the PlayerCharacter to move there
                if (rpgGameManager.isInGameSpace(y)){
                    rpgGameManager.setPlayerCharacterDestination(x, y);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Calls the rpgGameManager's draw method which will draw everything
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        rpgGameManager.draw(canvas);
    }

    /**
     * Initializes the rpgGameManager and starts the thread
     * @param holder The SurfaceHolder whose surface is being created
     */
    public void surfaceCreated(SurfaceHolder holder){
        rpgGameManager.initialize();
        this.gameThread.setRunning(true);
        this.gameThread.start();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
    }

    /**
     * Stops the game thread when the surface is destroyed
     * @param holder The SurfaceHolder whose surface has been destroyed
     */
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        while(retry){
            try{
                this.gameThread.setRunning(false);
                this.gameThread.join();
                retry = false;
            } catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}


