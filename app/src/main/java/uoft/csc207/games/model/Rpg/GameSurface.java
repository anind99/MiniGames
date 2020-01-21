package uoft.csc207.games.model.Rpg;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import uoft.csc207.games.activity.rpg.RpgActivity;
import uoft.csc207.games.controller.rpg.RPGGameManager;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback{
    private RpgActivity rpgActivity;
    private GameThread gameThread;

    public RPGGameManager getRpgGameManager() {
        return rpgGameManager;
    }

    private RPGGameManager rpgGameManager;

    public GameSurface(Context context){
        super(context);
        getHolder().addCallback(this);
        gameThread = new GameThread(this, getHolder());
        rpgActivity = (RpgActivity)context;
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;
        rpgGameManager = new RPGGameManager(context, width, height);
        setFocusable(true);
    }

    public GameThread getGameThread(){
        return gameThread;
    }

    public void update(){
        if (!rpgGameManager.isProcessingText()){
            rpgGameManager.update();
        }
    }

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

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        rpgGameManager.draw(canvas);
    }

    public void surfaceCreated(SurfaceHolder holder){
        rpgGameManager.initialize();
        this.gameThread.setRunning(true);
        this.gameThread.start();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
    }

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
            //retry = true;
        }
    }
}


