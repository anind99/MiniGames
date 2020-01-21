package uoft.csc207.games.model.Rpg;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public abstract class GameObject implements Comparable<GameObject>{
    protected Bitmap image;         //image of the entire sprite sheet

    protected final int rowCount;       //rows of single images
    protected final int colCount;       //columns of single images

    protected final int TOTAL_WIDTH;    //width of the entire sprite sheet
    protected final int TOTAL_HEIGHT;   //height of the entire sprite sheet

    protected final int singleWidth;    //width of a single image
    protected final int singleHeight;   //height of a single image

    protected int x;
    protected int y;

    public GameObject(Bitmap image, int rowCount, int colCount, int x, int y)  {
        this.image = image;
        this.rowCount = rowCount;
        this.colCount = colCount;

        this.x = x;
        this.y = y;

        this.TOTAL_WIDTH = image.getWidth();
        this.TOTAL_HEIGHT = image.getHeight();

        this.singleWidth = this.TOTAL_WIDTH / colCount;
        this.singleHeight = this.TOTAL_HEIGHT / rowCount;
    }

    protected Bitmap createSubImageAt(int row, int col){
        Bitmap subImage = Bitmap.createBitmap(image, col * singleWidth, row * singleHeight, singleWidth, singleHeight);
        return subImage;
    }

    /**
     *
     * @return x coordinate
     */
    public int getX(){ return this.x; }

    /**
     *
     * @return y coordinate
     */
    public int getY(){ return this.y; }

    /**
     * Gets the height of the individual walk cycle Bitmap images
     * @return image height
     */
    public int getHeight(){ return singleHeight; }

    /**
     * Gets the width of the individual walk cycle Bitmap images
     * @return image width
     */
    public int getWidth(){ return singleWidth; }

    public abstract void draw(Canvas canvas);

    /**
     * Compares GameObjects by Y coordinate
     * @param other The GameObject to compare with
     * @return -1, 0, 1, if the GameObject's Y coordinate is less than, equal to, or greater than
     * the other GameObject
     */
    public int compareTo(GameObject other){
        int result = 0;
        if (y > other.y){
            result = 1;
        } else if (y < other.y){
            result = -1;
        }
        return result;
    }
}
