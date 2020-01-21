package uoft.csc207.games.model.dodger;

import android.graphics.Canvas;

public interface GameObject {
    void draw(Canvas canvas);
    void update();
}
