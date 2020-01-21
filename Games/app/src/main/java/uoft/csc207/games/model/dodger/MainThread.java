package uoft.csc207.games.model.dodger;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread{
    /**
     * Thread for Scroller Game
     *
     * Fields
     * MAX_FPS: Static int - for frames per second
     * gamePanel: GamePanel - Surface of Scroller Game
     * running: Boolean - is game over?
     * canvas: Canvas - to be drawn on
     */

    private static final int MAX_FPS = 30;
    private final SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;
    public static Canvas canvas;


    MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel){
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
        this.running = true;
    }

    /**
     * loop for running game
     */
     @Override
     public void run(){
         long startTime;
         long timeMills;
         long waitTime;
         long targetTime = 1000/MAX_FPS;

         while(running){
             startTime = System.nanoTime();
             canvas = null;

             try{
                 canvas = this.surfaceHolder.lockCanvas();
                 synchronized (surfaceHolder){
                     this.gamePanel.update();
                     this.gamePanel.draw(canvas);
                     if (gamePanel.isOver){
                         running = false;
                     }
                 }
             } catch (Exception e){
                 e.printStackTrace();
             } finally{
                 if (canvas != null){
                     try {
                         surfaceHolder.unlockCanvasAndPost(canvas);
                     } catch (Exception e){
                         e.printStackTrace();
                     }
                 }
             }
             timeMills = (System.nanoTime() - startTime)/1000000;
             waitTime = targetTime - timeMills;
             try{
                 if (waitTime > 0){
                     sleep(waitTime);
                 }
             } catch (Exception e){
                 e.printStackTrace();
             }
         }
         try {
             sleep(3500);
         } catch (Exception e){
             e.printStackTrace();
         }
         if (!gamePanel.getScrollerActivity().exited) {
             gamePanel.getScrollerActivity().finishGame();
         }
     }

    /**
     * Setter for running
      * @param running Boolean
     */
    public void setRunning(boolean running){
        this.running = running;
    }


}
