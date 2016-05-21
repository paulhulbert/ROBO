import javafx.scene.image.Image;
import javafx.stage.Screen;

import java.awt.*;
import java.util.HashMap;

/**
 * Created by Paul on 5/25/2015.
 */
public class Constants {

    public static String leftKey = "A";
    public static String rightKey = "D";
    public static String jumpKey = "W";
    public static String primaryAttackKey = "SPACE";


    public static int framesPerSecond = 30;

    public static int mainPlayerX = 0;
    public static int mainPlayerY = 0;

    public static int mainPlayerLife = 100;


    public static int frameCount = 0;
    public static int secondCount = 0;
    public static int minuteCount = 0;
    public static int hourCount = 0;


    private static HashMap<String, Sprite> sprites = new HashMap<>();

    private static HashMap<String, Image> blockImages = new HashMap<>();


    public static double getXShift() {
        return -mainPlayerX + Screen.getPrimary().getBounds().getMaxX() / 2;
    }

    public static double getYShift() {
        return -mainPlayerY + Screen.getPrimary().getBounds().getMaxY() / 2;
    }


    public static Image getBlockImage( String pathName ) {


        if (sprites.containsKey(pathName)) {
            return  blockImages.get(pathName);
        }

        blockImages.put(pathName, new Image(pathName, false));

        return blockImages.get(pathName);
    }

    public static Sprite getSprite( String pathName ) {


        if (blockImages.containsKey(pathName)) {
            return  new Sprite(sprites.get(pathName));
        }

        sprites.put(pathName, new Sprite(pathName));

        return sprites.get(pathName);
    }



    public static boolean overlap(int x1, int y1, int width1, int height1, int x2, int y2, int width2, int height2) {


        Rectangle r1 = new Rectangle(x1, y1, width1, height1);
        Rectangle r2 = new Rectangle(x2, y2, width2, height2);


        return overlap(r1, r2);
    }


    public static boolean overlap(Rectangle r1, Rectangle r2) {
        return r1.contains(r2) || r1.intersects(r2);
    }


    public static void addFrame() {
        frameCount++;

        if (frameCount >= 60) {
            frameCount = 0;
            secondCount++;
            if (secondCount >= 60) {
                secondCount = 0;
                minuteCount++;
                if (minuteCount >= 60) {
                    minuteCount = 0;
                    hourCount++;
                }
            }
        }
    }

}
