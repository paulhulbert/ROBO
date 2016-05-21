import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Created by Paul on 5/25/2015.
 */
public class Block {

    private int x;
    private int y;
    private int width;
    private int height;

    private Image image;

    public Block(int x, int y, int width, int height, String pathname) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        image = Constants.getBlockImage(pathname);
    }



    public void step( GraphicsShell g ) {

        g.drawImage(image, x, y, width, height);

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

    public void hitByWeapon(Weapon weapon) {

    }
}
