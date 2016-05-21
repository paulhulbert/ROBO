
import javafx.scene.image.Image;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by Paul on 5/25/2015.
 */
public class Sprite {




    private ArrayList<Image> images = new ArrayList<>();

    private HashMap<String, ArrayList<Image>> animations = new HashMap<>();
    private HashMap<String, Integer> animationDelays = new HashMap<>();

    private int counter = 0;
    private String runningAnimation = "#run";

    private String runOnceAnimation = "";
    private boolean runningOnce = false;


    public Sprite(Sprite sprite) {

        images = sprite.images;
        animationDelays = sprite.animationDelays;
        animations = sprite.animations;

    }

    public Sprite(String filename) {


        Scanner scan = null;
        try {
            scan = new Scanner(new File("src/" + filename + "/ani.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String currentAnimation = "";

        while (scan.hasNext()) {
            String s = scan.nextLine().trim();
            if (s.charAt(0) == '#') {
                currentAnimation = s;
                animations.put(currentAnimation, new ArrayList<>());
                continue;
            }
            if (s.charAt(0) == '~') {
                s = scan.nextLine().trim();
                animationDelays.put(currentAnimation, Integer.parseInt(s));
                continue;
            }
        animations.get(currentAnimation).add(new Image( filename + "/" + s, false));
        }

    }

    public void step(GraphicsShell g, int x, int y, int width, int height, boolean facingLeft) {

        String animationToUse = runningAnimation;

        if (runningOnce) {
            animationToUse = runOnceAnimation;
        }

        if (animations.get(animationToUse).isEmpty()) {
            return;
        }

        if (counter >= animationDelays.get(animationToUse) * animations.get(animationToUse).size()) {
            counter = 0;
            runningOnce = false;
        }


        int slideNumber = (counter++) / animationDelays.get(animationToUse);
        if (facingLeft) {
            g.drawImage(animations.get(animationToUse).get(slideNumber), x, y, width, height);
        } else {
            g.drawImage(animations.get(animationToUse).get(slideNumber), x + width, y, -width, height);
        }

    }


    public void changeAnimation(String name) {
        if (!name.equals(runningAnimation) && animations.containsKey(name)) {
            runningAnimation = name;
            if (!runningOnce) {
                counter = 0;
            }
        }
    }


    public void animateOnce(String name) {


        if (animations.containsKey(name)) {
            runOnceAnimation = name;
            counter = 0;
            runningOnce = true;
        }
    }

    public boolean isRunningOnce() {
        return runningOnce;
    }

    public void setRunningOnce(boolean runningOnce) {
        this.runningOnce = runningOnce;
    }
}
