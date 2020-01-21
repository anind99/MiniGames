package uoft.csc207.games.model.dodger;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class RectObj implements  GameObject {
    /**
     * Represents One Rectangular Obstacle in Scroller, implements GameObject
     *
     * Fields
     * color: int - Color of rectangles
     * x: int - x position of rectangles
     */

    private int color;
    private int x;
    private Rect rect1;
    private Rect rect2;

    RectObj (int color, int PlayerHeight){
        this.x = Constants.SCREEN_WIDTH - 100;
        int rGap = (int)(Math.random()* Constants.SCREEN_HEIGHT)/2;
        this.rect1 = new Rect(x, 0, x + 100, rGap);
        this.rect2 = new Rect(x, (int)(rGap + 2 * PlayerHeight), x + 100, Constants.SCREEN_HEIGHT);
        this.color = color;
        }

    /**
     * Draws the obstacle
     * @param canvas: Canvas to be drawn on
     */
    public void draw(Canvas canvas){

        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rect1, paint);
        canvas.drawRect(rect2, paint);
    }

    /**
     * Updates Obstacle Position
     */
    public void update(){
        if (this.x > 0) {
            this.x -= Constants.SPEED;
            rect1.left -= Constants.SPEED;
            rect1.right -= Constants.SPEED;
            rect2.left -= Constants.SPEED;
            rect2.right -= Constants.SPEED;
        }
    }

    /**
     * Collision check with game character
     * @param s - scrollerCharacter to check collision with
     * @return true if collided
     */
    boolean Collide(scrollerCharacter s){
        Rect pRect = s.getRect();
        return (rect1.intersect(pRect) || rect2.intersect(pRect));
    }

    /**
     * @return x position plus 100
     */
    int getX() {
        return x + 100;
    }
}
