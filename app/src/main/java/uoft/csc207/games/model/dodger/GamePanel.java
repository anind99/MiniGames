package uoft.csc207.games.model.dodger;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import uoft.csc207.games.R;
import uoft.csc207.games.activity.dodger.ScrollerActivity;
import uoft.csc207.games.controller.ProfileManager;
import uoft.csc207.games.controller.Score;
import uoft.csc207.games.controller.ScoreBoard;
import uoft.csc207.games.model.IGameID;
import uoft.csc207.games.model.PlayerProfile;
import uoft.csc207.games.model.ScrollerGame;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private ScrollerActivity scrollerActivity;
    private MainThread thread;
    private ObsManager Obs;
    private int P_y;
    private scrollerCharacter player1;
    private Coin coins;
    boolean isOver;
    private Point po;
    private Boolean male;
    private PlayerProfile playerProfile;
    private ScrollerGame CurrentGame;
    private boolean start = false;

    public GamePanel(Context context){
        super(context);
        scrollerActivity = (ScrollerActivity)context;
        isOver = false;
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        player1 = new scrollerCharacter(BitmapFactory.decodeResource(getResources(),R.drawable.goku));
        this.coins = new Coin(6);
        P_y = Constants.SCREEN_HEIGHT/2;
        Obs = new ObsManager(3, Color.MAGENTA, player1.getHeight());
        System.out.println(player1.getHeight());
        this.playerProfile = ProfileManager.getProfileManager(context).getCurrentPlayer();
        InitCurrentGame();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,int height){

    }

    public ScrollerActivity getScrollerActivity(){
        return this.scrollerActivity;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        while (retry){
            try{
                thread.setRunning(false);
                thread.join();
            } catch (Exception e){
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (!isOver) {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE: {
                    if (start) {
                        P_y -= 50*Constants.SPEED;
                        CurrentGame.updateScore(CurrentGame.getScore() + 1);
                    } else {
                        po = new Point();
                        po.x = (int) e.getX();
                        po.y = (int) e.getY();
                    }

                }
            }
        }
        return true;
    }

    public MainThread getMainThread(){
        return thread;
    }

    public void startScreen(Canvas canvas){
        super.draw(canvas);
        Paint p1 = new Paint();
        p1.setColor(Color.BLACK);
        p1.setTextSize(100);
        canvas.drawColor(Color.GREEN);
        canvas.drawText("Big Text", 0, Constants.SCREEN_HEIGHT/3, p1);
        canvas.drawText("Regular Text", 0, 2*Constants.SCREEN_HEIGHT/3, p1);
        if ( po.y >= 0 && po.y <= Constants.SCREEN_HEIGHT/2){
            CurrentGame.chooseFont("Big");
            po.y = -1;
        }
        else if (po.y >= 0){
            CurrentGame.chooseFont("Small");
            po.y = -1;
        }
    }
    public void startScreen2(Canvas canvas){
        super.draw(canvas);
        Paint p1 = new Paint();
        p1.setColor(Color.BLACK);
        p1.setTextSize(100);
        canvas.drawColor(Color.GREEN);
        canvas.drawText("Male", 0, Constants.SCREEN_HEIGHT/3, p1);
        canvas.drawText("Female", 0, 2*Constants.SCREEN_HEIGHT/3, p1);
        if (po.y >= 0) {
            male = po.y <= Constants.SCREEN_HEIGHT / 2;
            CurrentGame.chooseCharacter("Male");
            po.y = -1;
        }
        if (!male){
            player1 = new scrollerCharacter(BitmapFactory.decodeResource(getResources(),R.drawable.fem));
            CurrentGame.chooseCharacter("Female");
        }
    }

    private void startScreen3(Canvas canvas){
        super.draw(canvas);
        Paint p1 = new Paint();
        p1.setColor(Color.BLACK);
        p1.setTextSize(100);
        canvas.drawColor(Color.GREEN);
        canvas.drawText("Blue", 0, Constants.SCREEN_HEIGHT/3, p1);
        canvas.drawText("Red", 0, 2*Constants.SCREEN_HEIGHT/3, p1);
        if ( po.y >= Constants.SCREEN_HEIGHT/2){
            CurrentGame.chooseColor("RED");
            po.y = -1;
        }
        else if (po.y >= 0){
            CurrentGame.chooseColor("BLUE");
            po.y = -1;
        }
    }

    private void startScreen4(Canvas canvas){
        super.draw(canvas);
        Paint p1 = new Paint();
        p1.setColor(Color.BLACK);
        p1.setTextSize(100);
        canvas.drawColor(Color.GREEN);
        canvas.drawText("Advanced", 0, Constants.SCREEN_HEIGHT/3, p1);
        canvas.drawText("Regular", 0, 2*Constants.SCREEN_HEIGHT/3, p1);
        if ( po.y >= Constants.SCREEN_HEIGHT/2){
            Constants.SPEED = 1;
            po.y = -1;
        }
        else if (po.y >= 0){
            Constants.SPEED = 2;
            po.y = -1;
        }
    }

    @Override
    public void draw(Canvas canvas){
        startGame(canvas);
        super.draw(canvas);
        Paint p = new Paint();
        if (isOver) {
            GameOverScreen(canvas, p);
        } else{
            p.setColor(Color.BLACK);
            if (CurrentGame.getFont().equals("Big")) {
                p.setTextSize(100);
            } else {
                p.setTextSize(50);
            }
            if (CurrentGame.getColor().equals("RED")){
                canvas.drawColor(Color.RED);
            } else{
                canvas.drawColor(Color.BLUE);
            }
            Obs.draw(canvas);
            player1.draw(canvas);
            coins.draw(canvas);
            canvas.drawText("Score: " + CurrentGame.getScore(), 100, 50 + p.descent() - p.ascent(), p);
            canvas.drawText("Money: " + CurrentGame.getCurrency(), 100, 200 + p.descent() - p.ascent(), p);
        }

        }

    private void startGame(Canvas canvas){
        if (CurrentGame.getFont() == null) {
            startScreen(canvas);
        } else if (CurrentGame.getCharacter() == null) {
            startScreen2(canvas);
        } else if (CurrentGame.getColor() == null) {
            startScreen3(canvas);
        } else if (Constants.SPEED == 0) {
            startScreen4(canvas);
        } else{
            start = true;
        }
    }

        private void GameOverScreen(Canvas canvas, Paint p){
            canvas.drawColor(Color.BLACK);
            p.setColor(Color.YELLOW);
            p.setStrokeWidth(10);
            p.setTextSize(100);
            canvas.drawText("GAME OVER!", Constants.SCREEN_WIDTH/4, Constants.SCREEN_HEIGHT /2, p);
            canvas.drawText("Scroller Points: " + CurrentGame.getScore(), 50, 50 + p.descent() - p.ascent(), p);
            canvas.drawText("Scroller Money: " + CurrentGame.getCurrency(), 50, 350 + p.descent() - p.ascent(), p);
        }

        private void gameOver(){
            isOver = true;
            Constants.SPEED = 0;
            CurrentGame.checkAchievements();
            CurrentGame.setCumulativeCurrency(CurrentGame.getCumulativeCurrency()+CurrentGame.getCurrency());
            CurrentGame.setCumulativeScore(CurrentGame.getCumulativeScore()+CurrentGame.getScore());
            ScoreBoard.setCurrentScore(new Score("", CurrentGame.getScore(), CurrentGame.getCurrency(),
                    ScrollerActivity.class.getName()));
        }


    public void update() {
        if (!isOver && start) {
            if (!Obs.detectCollision(player1)) {
                if (P_y + player1.getHeight() < Constants.SCREEN_HEIGHT) {
                    P_y += Constants.SPEED * 5;
                }
                player1.update(P_y);
                Obs.update();
                coins.update();
                coins.CollisionCheck(player1, CurrentGame);
            } else {
                gameOver();
            }

        }
    }

    private void InitCurrentGame(){
        ScrollerGame s = (ScrollerGame) playerProfile.containsGame(IGameID.DODGER);
        if (s == null){
            s = new ScrollerGame();
            playerProfile.addGame(s);
        }
        CurrentGame = s;
        s.updateCurrency(0);
        s.updateScore(0);
    }

}