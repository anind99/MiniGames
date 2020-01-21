package uoft.csc207.games.model.dodger;

import android.graphics.Canvas;

public interface GameObject {

    /**
     * Interface for Objects in ScrollerGame
     */

    /**
     * draw method to be implemented
     * @param canvas to be drawn on
     */
    void draw(Canvas canvas);

    /**
     * updated method to be implemented
     */
    void update();
}
