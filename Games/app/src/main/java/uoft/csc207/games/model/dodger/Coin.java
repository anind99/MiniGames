package uoft.csc207.games.model.dodger;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;

public class Coin implements GameObject{
    /**
     * Class for all coins in Scroller game, implements GameObject
     *
     * Fields
     * numCoins: number of coins currently available
     * coins: ArrayList<Rect> - List of all the coins
     */

    private ArrayList<Rect> coins;
    private int numCoins;

    Coin (int numCoins){
        this.numCoins = numCoins;
        coins = new ArrayList<>();
        for (int i = 0; i < numCoins; i++){
            int x = (int)(Math.random() * Constants.SCREEN_WIDTH - 50);
            int y = 50 + (int)(Math.random() * Constants.SCREEN_HEIGHT - 50);
            Rect r = new Rect(x, y, x + 50, y + 50);
            coins.add(r);
        }
    }

    /**
     * draw method for coins
     * @param canvas: Canvas to be drawn on
     */
    public void draw(Canvas canvas){

        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        for (Rect r: coins){
            canvas.drawRect(r, paint);
        }
    }

    /**
     * update method for coins, removed when coin touches left boundary
     */
    public void update(){
        for (Rect r: coins){
            if (r.left > 0) {
                r.left -= Constants.SPEED;
                r.right -= Constants.SPEED;
            } else{
                coins.remove(r);
            }
        }
        if (coins.size() == 0){
            for (int i = 0; i < numCoins; i++){
                int x = (int)(Math.random() * Constants.SCREEN_WIDTH - 50);
                int y = 50 + (int)(Math.random() * Constants.SCREEN_HEIGHT - 50);
                Rect r = new Rect(x, y, x + 50, y + 50);
                coins.add(r);
            }
        }
    }


    /**
     * Checks for collision with Scroller Character
     * @param s Character to check collision for
     * @param g Game for which money must be updated
     */
    void CollisionCheck(scrollerCharacter s, ScrollerGame g){
        Rect pRect = s.getRect();
        for (Rect r: coins) {
            if (r.intersect(pRect)) {
                coins.remove(r);
                g.updateCurrency(g.getGameCurrency() + 2);
            }
        }
    }

}

