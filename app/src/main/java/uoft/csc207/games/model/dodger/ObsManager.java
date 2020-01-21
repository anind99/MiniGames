package uoft.csc207.games.model.dodger;

import android.graphics.Canvas;

import java.util.ArrayList;

public class ObsManager {
    private ArrayList<RectObj> Obs = new ArrayList<>();
    private int Color;
    private int NumObs;
    private int gap;
    private int PlayerHeight;

    public ObsManager(int numObs, int color, int playerHeight){
        this.Color = color;
        this.NumObs = numObs;
        this.PlayerHeight = playerHeight;
        this.gap = Constants.SCREEN_WIDTH - (Constants.SCREEN_WIDTH/(numObs -1));
        RectObj r = new RectObj(color, playerHeight);
        Obs.add(r);
    }

    public boolean update() {
        for (RectObj r : Obs) {
            if (r.getX() <= 100) {
                Obs.remove(r);
            }
            else {
                r.update();
            }
        }
        if (Obs.get(Obs.size() -1).getX() <= this.gap + 100){
            RectObj r = new RectObj(this.Color, this.PlayerHeight);
            Obs.add(r);
        }
        return true;
    }

    public void draw(Canvas can){
        for (RectObj o: Obs){
            o.draw(can);
        }
    }

    public boolean detectCollision(scrollerCharacter s){
        for (RectObj o: Obs){
            if(o.Collide(s)) {
                System.out.println("Collided");
                return true;
            }
        }
        return false;
    }
}
