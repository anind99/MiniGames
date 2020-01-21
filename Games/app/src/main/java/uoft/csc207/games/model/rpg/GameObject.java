package uoft.csc207.games.model.rpg;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * The superclass for all game entities that will be part of the game
 */
public abstract class GameObject implements Comparable<GameObject>{
    protected Bitmap image;         //image of the entire sprite sheet

    protected final int rowCount;       //rows of single images
    protected final int colCount;       //columns of single images

    protected final int TOTAL_WIDTH;    //width of the entire sprite sheet
    protected final int TOTAL_HEIGHT;   //height of the entire sprite sheet

    protected final int singleWidth;    //width of a single image
    protected final int singleHeight;   //height of a single image

    protected int x;    //x coordinate of the GameObject
    protected int y;    //y coordinate of the GameObject

    /**
     * Creates a GameObject with the image used to splice its possible images, its position, and information
     * on the passed in image that will be used to extract sub images from if necessary.
     * @param image The sprite sheet of the GameObject
     * @param rowCount The number of rows in the sprite sheet
     * @param colCount The number of columns in the sprite sheet
     * @param x The x coordinate
     * @param y The y coordinate
     */
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

    /**
     * Creates and returns a sub image from the GameObject's sprite sheet
     * @param row The row of the sprite sheet to create the sub image
     * @param col The column of the sprite sheet to create the sub image
     * @return The sub image created from the given location of the sprite sheet
     */
    protected Bitmap createSubImageAt(int row, int col){
        Bitmap subImage = Bitmap.createBitmap(image, col * singleWidth, row * singleHeight, singleWidth, singleHeight);
        return subImage;
    }

    public int getX(){ return this.x; }
    public int getY(){ return this.y; }

    /**
     * Gets the height of an individual walk cycle Bitmap image
     * @return image height
     */
    public int getHeight(){ return singleHeight; }

    /**
     * Gets the width of an individual walk cycle Bitmap image
     * @return image width
     */
    public int getWidth(){ return singleWidth; }

    /**
     * How a given GameObject will choose to draw itself on the canvas
     * @param canvas The canvas for the GameObject to draw itself on
     */
    public abstract void draw(Canvas canvas);

    /**
     * Compares GameObjects by Y coordinate which will be relevant for draw ordering and which GameObject
     * should cover which depending on their screen position.
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
