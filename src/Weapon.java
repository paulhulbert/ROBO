import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Paul on 5/27/2015.
 */
public class Weapon {

    private boolean active = true;

    private boolean facingLeft = true;

    private boolean piercing = false;
    private boolean capableOfMultiHit = false;


    private int disappearAfter = -1;

    private int damage;


    private int x;
    private int y;

    private int width;
    private int height;

    private double xVelocity = 0;
    private double yVelocity = 0;

    private double xAcceleration = 0;
    private double yAcceleration = 0;


    private Sprite sprite;

    private ArrayList<Rectangle> hitBoxes = new ArrayList<>();

    public Weapon(int x, int y, int width, int height, int damage, String spritePathName) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.damage = damage;
        this.sprite = Constants.getSprite(spritePathName);

        addHitBox(0, 0, width, height);
    }


    public void step(GraphicsShell g) {

        if (!active) {
            return;
        }

        if (disappearAfter > 0) {
            disappearAfter--;
        }

        if (disappearAfter == 0) {
            active = false;
        }

        xVelocity += xAcceleration;
        yVelocity += yAcceleration;

        x += xVelocity;
        y += yVelocity;

        if (xVelocity > 0) {
            setFacingLeft(false);
        }
        if (xVelocity < 0) {
            setFacingLeft(true);
        }


        sprite.step(g, x, y, width, height, facingLeft);
    }


    public boolean getActive() {
        return active;
    }

    private void addHitBox(int x, int y, int width, int height) {
        hitBoxes.add(new Rectangle(x, y, width, height));
    }

    private void removeHitBox(int x, int y, int width, int height) {
        hitBoxes.remove(new Rectangle(x, y, width, height));
    }


    public void checkContact(TargetBox targetBox) {

        if (!active) {
            return;
        }

        for (Rectangle rectangle : hitBoxes) {


            if (targetBox.hit(new Rectangle(rectangle.x + getX(), rectangle.y + getY(),
                    rectangle.width, rectangle.height))) {

                targetBox.getAttachedActor().hitByWeapon(this);

                if (!piercing) {
                    active = false;
                }
                if (!capableOfMultiHit) {
                    return;
                }
            }

        }

    }

    public void checkContact(Block block) {

        if (!active) {
            return;
        }

        for (Rectangle rectangle : hitBoxes) {


            if (Constants.overlap(new Rectangle(block.getX(), block.getY(), block.getWidth(), block.getHeight()),
                    new Rectangle(rectangle.x + getX(), rectangle.y + getY(),
                    rectangle.width, rectangle.height))) {

                block.hitByWeapon(this);

                active = false;

                if (!capableOfMultiHit) {
                    return;
                }
            }

        }

    }

    @Override
    public boolean equals(Object o) {

        Weapon w = (Weapon) o;

        return w.x == x && w.y == y && w.width == width && w.height == height;

    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getxVelocity() {
        return xVelocity;
    }

    public void setxVelocity(double xVelocity) {

        if (this.xVelocity >= 0 && xVelocity < 0) {
            x -= width;
        }

        this.xVelocity = xVelocity;
    }

    public double getyVelocity() {
        return yVelocity;
    }

    public void setyVelocity(double yVelocity) {
        this.yVelocity = yVelocity;
    }

    public double getxAcceleration() {
        return xAcceleration;
    }

    public void setxAcceleration(double xAcceleration) {
        this.xAcceleration = xAcceleration;
    }

    public double getyAcceleration() {
        return yAcceleration;
    }

    public void setyAcceleration(double yAcceleration) {
        this.yAcceleration = yAcceleration;
    }

    public boolean isCapableOfMultiHit() {
        return capableOfMultiHit;
    }

    public void setCapableOfMultiHit(boolean capableOfMultiHit) {
        this.capableOfMultiHit = capableOfMultiHit;
    }

    public boolean isPiercing() {
        return piercing;
    }

    public void setPiercing(boolean piercing) {
        this.piercing = piercing;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getDisappearAfter() {
        return disappearAfter;
    }

    public void setDisappearAfter(int disappearAfter) {
        this.disappearAfter = disappearAfter;
    }

    public boolean isFacingLeft() {
        return facingLeft;
    }

    public void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
    }
}
