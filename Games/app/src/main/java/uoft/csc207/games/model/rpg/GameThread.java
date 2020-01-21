package uoft.csc207.games.model.rpg;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * The game thread used to consistently update the game.
 */
public class GameThread extends Thread{
    private boolean running;
    private GameSurface gameSurface;
    private SurfaceHolder surfaceHolder;

    /**
     * Creates a thread for the rpg game
     * @param gS The SurfaceView for the rpg game
     * @param sH The given SurfaceHolder containing the canvas
     */
    public GameThread(GameSurface gS, SurfaceHolder sH){
        this.gameSurface = gS;
        this.surfaceHolder = sH;
    }

    /**
     * Used to constantly update and draw and redraw the appropriate things on the canvas
     */
    public void run(){
        long startTime = System.nanoTime();

        while(running){
            Canvas canvas = null;
            try{
                //Gets Canvas from Holder and locks it
                canvas = this.surfaceHolder.lockCanvas();

                synchronized(canvas){
                    this.gameSurface.update();
                    this.gameSurface.draw(canvas);
                }
            } catch(Exception e){
                e.printStackTrace();

            } finally {
                if(canvas != null){
                    this.surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
            long currentTime = System.nanoTime();

            long waitTime = (currentTime - startTime) / 1000000;
            if (waitTime < 10){
                waitTime = 10;
            }

            try{
                //this.sleep(waitTime);
                this.sleep(10);
            } catch(InterruptedException e){
            }
            startTime = System.nanoTime();
        }
    }

    public void setRunning(boolean isMoving){
        running = isMoving;
    }
}
