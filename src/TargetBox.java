

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Paul on 5/27/2015.
 */
public class TargetBox {

    private Actor attachedActor;

    private ArrayList<Rectangle> areas = new ArrayList<>();

    public TargetBox(Actor attachedActor) {
        this.attachedActor = attachedActor;
    }

    public void addArea(int x, int y, int width, int height) {
        areas.add(new Rectangle(x, y, width, height));
    }

    public void removeArea(int x, int y, int width, int height) {
        areas.remove(new Rectangle(x, y, width, height));
    }


    public boolean hit(Rectangle r) {
        for (Rectangle rectangle : areas) {
            if (Constants.overlap(r, new Rectangle(rectangle.x + attachedActor.getX(), rectangle.y + attachedActor.getY(),
                    rectangle.width, rectangle.height))) {
                return true;
            }
        }

        return false;
    }


    public Actor getAttachedActor() {
        return attachedActor;
    }
}
