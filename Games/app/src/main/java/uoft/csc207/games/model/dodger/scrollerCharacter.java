package uoft.csc207.games.model.dodger;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;


public class scrollerCharacter implements GameObject {
    /**
     * Class for character in Scroller game, Implements GameObject
     *
     * Fields
     * Bitmap img: image of character
     * x: int -  x position of character
     * y: int - y position of character
     * width: int - width of character
     * height: int - height of character
     */
    private Bitmap img;
    private int x;
    private int y;
    private int width;
    private int height;


    scrollerCharacter(Bitmap img){
            x = Constants.SCREEN_WIDTH/2;
            y = Constants.SCREEN_HEIGHT/2;
            width = Constants.SCREEN_WIDTH/6;
            int scale = img.getWidth()/width;
            height = img.getHeight()/scale;
            this.img = Bitmap.createScaledBitmap(img, width,
                    height, true);
    }

    public void update(){}

    /**
     * @param y - updates the y position of character
     */
    public void update(int y){
        if (y < Constants.SCREEN_HEIGHT - height) {
            this.y = y;
        }
    }

    /**
     * Draws character
     * @param canvas to be drawn on
     */
    public void draw(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        canvas.drawBitmap(img, x, y, p);
    }

    /**
     * @return height
     */
    int getHeight(){return this.height;}

    /**
     * @return Rect object with character dimensions
     */
    Rect getRect(){
        return new Rect(x, y, x + width, y + height);
    }
}
